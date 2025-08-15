package com.baghdad.viewmodel.trendingMovie

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.entity.media.Genre
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
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<TrendingMoviesScreenState, TrendingMoviesEffect>(TrendingMoviesScreenState()),
    TrendingMoviesInteractionListener {

    init {
        checkIfUserIsLoggedIn()
        loadGenres()
        loadMoviesByGenres(null)
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
            dispatcher = defaultDispatcher
        )
    }

    private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
        updateState {
            it.copy(isUserLoggedIn = isLoggedIn)
        }
        if (isLoggedIn) {
            getUserSavedLists()
        }
    }

    private fun getUserSavedLists() {
        collectPagingFlow(
            loadData = { page -> getSavedListsUseCase(page = page, pageSize = DEFAULT_PAGE_SIZE) },
            onInitialLoadError = ::onLoadDataError,
            pageSize = DEFAULT_PAGE_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::onGetSavedListFlowCreated
        )
    }

    private fun onGetSavedListFlowCreated(flow: Flow<PagingData<SavedListUiState>>) {
        updateState {
            it.copy(
                addToListBottomSheetState = it.addToListBottomSheetState.copy(savedLists = flow)
            )
        }
    }

    private fun loadGenres() {
        tryToExecute(
            callee = { getMovieGenresUseCase.getMovieGenres() },
            onSuccess = ::handleGenreSuccess,
            onError = ::onLoadDataError,
            dispatcher = defaultDispatcher
        )
    }

    private fun loadMoviesByGenres(categoryId: Long?) {
        updateState { it.copy(isLoading = true, selectedGenreId = categoryId) }
        collectPagingFlow(
            loadData = { page ->
                getTrendingMoviesUseCase.invoke(
                    genreId = currentState.selectedGenreId,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMovieUiState() },
            onFlowCreated = { flow ->
                updateState { it.copy(movies = flow) }
                hideSnackBar()
            },
            onInitialLoadError = ::onLoadDataError
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong()
        )
    }

    private fun handleGenreSuccess(genres: List<Genre>) {
        val categoryList = genres.map(Genre::toGenreUiState)

        updateState { it.copy(categories = categoryList) }
    }

    override fun onBackClicked() {
        sendEffect(TrendingMoviesEffect.NavigateBack)
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(TrendingMoviesEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSaveMovieClick(movie: TrendingMoviesScreenState.TrendingMovieUiState) {
        onSaveButtonClicked(
            listId = movie.savedListId,
            itemId = movie.id,
            isSaved = movie.isSaved
        )
    }

    private fun onSaveButtonClicked(
        listId: Long,
        itemId: Long,
        isSaved: Boolean
    ) {
        if (isSaved) {
            removeSavedItem(listId = listId, itemId = itemId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            isVisible = true,
                            selectedItemId = itemId,
                            selectedListId = null
                        )
                )
            }
        }
    }

    private fun removeSavedItem(
        listId: Long,
        itemId: Long,
    ) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(listId = listId, movieId = itemId) },
            onSuccess = { onRemoveSavedItemSuccess() },
            dispatcher = defaultDispatcher,
            onFinally = ::onRemoveSavedItemFinished,
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

    private fun onRemoveSavedItemFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false
                    )
            )
        }
    }

    override fun onCategoryClicked(categoryId: Long?) {
        if (categoryId != currentState.selectedGenreId) {
            loadMoviesByGenres(categoryId)
        }
    }

    override fun onSnackBarActionLabelClicked(categoryId: Long?) {
        hideSnackBar()
        loadGenres()
        loadMoviesByGenres(categoryId)
    }

    override fun onSaveItemToListClicked() {
        tryToExecute(
            callee = {
                addMovieToSavedListUseCase(
                    listId = currentState.addToListBottomSheetState.selectedListId
                        ?: return@tryToExecute,
                    movieId = currentState.addToListBottomSheetState.selectedItemId,
                )
            },
            onError = { onAddItemToListError() },
            onSuccess = { onAddItemToListSuccess() },
            dispatcher = defaultDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished
        )
    }

    fun onAddItemToListError() {
        showNoInternetSnackBarWithoutRetry()

    }

    private fun showNoInternetSnackBarWithoutRetry() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            isSuccess = false,
        )
    }

    private fun onAddItemToListSuccess() {
        refreshSavedItems()
        onSaveToListBottomSheetDismiss()
        showItemSavedSuccessfullySnackBar()
    }

    private fun refreshSavedItems() {
        loadMoviesByGenres(currentState.selectedGenreId)
        getUserSavedLists()
    }

    private fun showItemSavedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true
        )
    }

    private fun onAddItemToListStart() {
        updateState {
            it.copy(
                addToListBottomSheetState = it.addToListBottomSheetState.copy(isLoading = true)
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
                addListBottomSheetState = it.addListBottomSheetState.copy(isVisible = true),
                addToListBottomSheetState = it.addToListBottomSheetState.copy(isVisible = false)
            )
        }
    }

    override fun onLoginClicked() {
        sendEffect(TrendingMoviesEffect.NavigateToLogin)
    }

    override fun onSaveToListBottomSheetDismiss() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    AddToListBottomSheetState(savedLists = it.addToListBottomSheetState.savedLists)
            )
        }
    }

    override fun onListSelected(listId: Long) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(selectedListId = listId)
            )
        }
    }

    override fun onCreatedListNameChanged(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState = it.addListBottomSheetState.copy(listName = name)
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
                addToListBottomSheetState = it.addToListBottomSheetState.copy(isVisible = true)
            )
        }
    }

    override fun onCreateListBottomSheetAddClick() {
        tryToExecute(
            callee = {
                createSavedListUseCase(title = currentState.addListBottomSheetState.listName)
            },
            onSuccess = { onCreateListSuccess() },
            dispatcher = defaultDispatcher,
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished
        )
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onCreateListStart() {
        updateState {
            it.copy(addListBottomSheetState = it.addListBottomSheetState.copy(isLoading = true))
        }
    }

    private fun onCreateListFinished() {
        updateState {
            it.copy(addListBottomSheetState = it.addListBottomSheetState.copy(isLoading = false))
        }
    }


    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}