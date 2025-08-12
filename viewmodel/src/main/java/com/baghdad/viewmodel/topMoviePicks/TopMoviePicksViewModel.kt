package com.baghdad.viewmodel.topMoviePicks

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.actor.GetActorMoviesUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.SavedListUiState
import com.baghdad.viewmodel.shared.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopMoviePicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorMoviesUseCase: GetActorMoviesUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val removeMovieFromListUseCase: RemoveMovieFromSavedListUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
) : BaseViewModel<TopMoviePicksState, TopMoviePicksEffect>
    (TopMoviePicksState()), TopMoviePicksInteractionListener {

    private val actorId: Long = checkNotNull(savedStateHandle["actorId"])

    init {
        loadData()
    }

    private fun loadData() {
        checkIfUserIsLoggedIn()
        getActorMovies(actorId)
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
            dispatcher = ioDispatcher,
        )
    }

    private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
        updateState { it.copy(isUserLoggedIn = isLoggedIn) }
        if (isLoggedIn) {
            getUserSavedLists()
        }
    }

    private fun getActorMovies(actorId: Long) {
        tryToExecute(
            callee = { getActorMoviesUseCase(actorId) },
            dispatcher = ioDispatcher,
            onSuccess = ::onGetActorMoviesSuccess,
            onError = ::onGetActorMoviesError,
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun onGetActorMoviesSuccess(movies: List<SavedMovie>) {
        hideSnackBar()
        updateState { topMoviePicksState ->
            topMoviePicksState.copy(movies = movies.map { it.toUIState() })
        }
    }

    private fun onGetActorMoviesError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    override fun onMovieDetailsClicked(movieId: Long) {
        sendEffect(TopMoviePicksEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSaveMovieClicked(item: TopMoviePicksState.MovieUiState) {
        onSaveButtonClicked(item.id, item.savedListId, item.isSaved)
    }

    private fun onSaveButtonClicked(
        movieId: Long,
        listId: Long,
        isSaved: Boolean
    ) {
        if (isSaved) {
            removeMovieFromList(listId, movieId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            selectedItemId = movieId,
                            isVisible = true,
                            selectedListId = null
                        )
                )
            }
        }
    }

    override fun onBackClicked() {
        sendEffect(TopMoviePicksEffect.NavigateBack)
    }

    override fun onSnackBarActionLabelClicked() {
        hideSnackBar()
        loadData()
    }

    override fun onSaveItemToListClicked() {
        tryToExecute(
            callee = {
                addMovieToSavedListUseCase(
                    listId =
                        currentState.addToListBottomSheetState.selectedListId
                            ?: return@tryToExecute,
                    movieId = currentState.addToListBottomSheetState.selectedItemId,
                )
            },
            onSuccess = { onAddItemToListSuccess() },
            dispatcher = ioDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
        )
    }

    private fun removeMovieFromList(
        listId: Long,
        movieId: Long
    ) {
        tryToExecute(
            callee = { removeMovieFromListUseCase(listId = listId, movieId = movieId) },
            onSuccess = { onRemoveSavedItemSuccess() },
            dispatcher = ioDispatcher,
        )
    }

    private fun onRemoveSavedItemSuccess() {
        refreshSavedItems()
        showItemRemovedSuccessfullySnackBar()
    }

    private fun showItemRemovedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onAddItemToListSuccess() {
        refreshSavedItems()
        onSaveToListBottomSheetDismiss()
    }

    private fun refreshSavedItems() {
        loadData()
        getUserSavedLists()
    }

    private fun onAddItemToListStart() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = true,
                    )
            )
        }
    }

    private fun onAddItemToListFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState = it.addToListBottomSheetState.copy(isLoading = false)
            )
        }
    }


    override fun onCreateNewListClicked() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = true
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false
                    )
            )
        }
    }

    override fun onLoginClicked() {
        sendEffect(
            TopMoviePicksEffect.NavigateToLogin
        )
    }

    override fun onSaveToListBottomSheetDismiss() {
        updateState {
            it.copy(
                addToListBottomSheetState = AddToListBottomSheetState(
                    savedLists = it.addToListBottomSheetState.savedLists
                )
            )
        }
    }

    override fun onListSelected(listId: Long) {
        updateState {
            it.copy(
                addToListBottomSheetState = it.addToListBottomSheetState.copy(
                    selectedListId = listId
                )
            )
        }
    }

    override fun onCreatedListNameChanged(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        listName = name
                    )
            )
        }
    }

    override fun onCreateListBottomSheetDismiss() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = false,
                        listName = "",
                        isLoading = false,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = true
                    )
            )
        }
    }

    override fun onCreateListBottomSheetAddClick() {
        tryToExecute(
            callee = {
                createSavedListUseCase(
                    title = currentState.addListBottomSheetState.listName,
                )
            },
            dispatcher = ioDispatcher,
            onSuccess = {
                onCreateListSuccess()
            },
            onError = ::onGetActorMoviesError,
            onStart = ::onLoading,
            onFinally = ::onFinally,
        )
    }

    private fun getUserSavedLists() {
        collectPagingFlow(
            loadData = { page ->
                getSavedListsUseCase(
                    page = page,
                    pageSize = DEFAULT_PAGE_SIZE,
                )
            },
            onInitialLoadError = ::onLoadDataError,
            pageSize = DEFAULT_PAGE_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::onGetSavedListFlowCreated,
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun onGetSavedListFlowCreated(flow: Flow<PagingData<SavedListUiState>>) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        savedLists = flow
                    )
            )
        }
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
