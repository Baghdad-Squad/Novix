package com.baghdad.viewmodel.continueWatching

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingByGenreUseCase
import com.baghdad.domain.usecase.continueWatching.GetAllContinueWatchingUseCase
import com.baghdad.domain.usecase.continueWatching.GetCurrentContinueWatchingMovieGenres
import com.baghdad.domain.usecase.continueWatching.GetCurrentContinueWatchingTvShowGenres
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
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
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getCurrentContinueWatchingTvShowGenres: GetCurrentContinueWatchingTvShowGenres,
    private val getCurrentContinueWatchingMovieGenres: GetCurrentContinueWatchingMovieGenres,
    private val getAllContinueWatchingUseCase: GetAllContinueWatchingUseCase,
    private val getAllContinueWatchingByGenreUseCase: GetAllContinueWatchingByGenreUseCase,
    private val isUserLoggedInUseCase: IsLoggedInUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) : BaseViewModel<ContinueWatchingState, ContinueWatchingScreenEffect>(ContinueWatchingState()),
    ContinueWatchingInteractionListener {
    init {
        checkIfUserIsLoggedIn()
        getGenres()
        getMedia(null)
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
            dispatcher = defaultDispatcher,
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
            loadData = { page ->
                getSavedListsUseCase(
                    page = page,
                    pageSize = DEFAULT_PAGE_SIZE,
                )
            },
            onInitialLoadError = ::onGetGenresError,
            pageSize = DEFAULT_PAGE_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::onGetSavedListFlowCreated,
        )
    }

    private fun onGetSavedListFlowCreated(flow: Flow<PagingData<SavedListUiState>>) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        savedLists = flow,
                    ),
            )
        }
    }

    private fun getGenres() {
        tryToCollect(
            { if (currentState.selectedMediaTabIsMovie) getCurrentContinueWatchingMovieGenres.invoke() else getCurrentContinueWatchingTvShowGenres.invoke() },
            ::onGenresFetched,
            onError = ::onGetGenresError,
        )
    }

    private fun onGetGenresError(throwable: Throwable) {
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
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun getMedia(genreId: Long?) {
        collectPagingFlow(
            { page -> onGetMedia(genreId, page) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { mediaFlow -> updateState { it.copy(mediaFlow) } },
            onLoadingChanged = ::onGetMediaLoadingChanged,
        )
    }

    private fun onGetMediaLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    private suspend fun onGetMedia(
        genreId: Long?,
        page: Int,
    ): PagedResult<ContinueWatching> {
        val result =
            if (genreId == null) {
                getAllContinueWatchingUseCase(page)
            } else {
                getAllContinueWatchingByGenreUseCase(genreId, page)
            }

        val filteredData =
            result.data.filter { item ->
                (item.contentType == ContinueWatching.ContentType.MOVIE && currentState.selectedMediaTabIsMovie) ||
                        (item.contentType == ContinueWatching.ContentType.TV_SHOW && !currentState.selectedMediaTabIsMovie)
            }

        return result.copy(data = filteredData)
    }

    private fun onGenresFetched(genres: List<Genre>) {
        hideSnackBar()
        updateState {
            it.copy(
                genres =
                    genres
                        .distinctBy { genre -> genre.id }
                        .map { genre -> genre.toContinueWatchingUiState() },
            )
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.DefaultMessage

    override fun onBackClick() {
        sendEffect(ContinueWatchingScreenEffect.NavigateBack)
    }

    override fun onMediaClick(
        mediaId: Long,
        contentType: ContinueWatchingState.ContinueWatchingMovieUiState.ContentType,
    ) {
        if (contentType == ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.MOVIE) {
            sendEffect(ContinueWatchingScreenEffect.NavigateToMovieDetails(mediaId))
        } else {
            sendEffect(ContinueWatchingScreenEffect.NavigateToTvShowDetails(mediaId))
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onGenreClick(genreId: Long?) {
        if (currentState.selectedMediaTabIsMovie) {
            handleGenreSelection(
                currentSelectedId = currentState.selectedMovieGenreId,
                newGenreId = genreId,
                update = { id ->
                    updateState {
                        it.copy(
                            selectedMovieGenreId = id,
                            isLoading = true,
                            mediaFlow = flowOf(),
                        )
                    }
                },
            )
        } else {
            handleGenreSelection(
                currentSelectedId = currentState.selectedTvShowGenreId,
                newGenreId = genreId,
                update = { id ->
                    updateState {
                        it.copy(
                            selectedTvShowGenreId = id,
                            isLoading = true,
                            mediaFlow = flowOf(),
                        )
                    }
                },
            )
        }
    }

    private fun handleGenreSelection(
        currentSelectedId: Long?,
        newGenreId: Long?,
        update: (Long?) -> Unit,
    ) {
        if (newGenreId != currentSelectedId) {
            update(newGenreId)
            getMedia(newGenreId)
        }
    }

    override fun onSelectedTab(isMovieTab: Boolean) {
        val genreId =
            if (isMovieTab) currentState.selectedMovieGenreId else currentState.selectedTvShowGenreId

        updateState {
            it.copy(
                selectedMediaTabIsMovie = isMovieTab,
                isLoading = true,
                mediaFlow = flowOf(),
            )
        }
        getGenres()
        getMedia(genreId)
    }

    override fun onMovieSaveClick(movie: ContinueWatchingState.ContinueWatchingMovieUiState) {
        onSaveMovieClick(movie.isSaved, movie.savedListId, movie.id)
    }

    private fun onSaveMovieClick(
        isSaved: Boolean,
        listId: Long,
        movieId: Long,
    ) {
        if (isSaved) {
            removeSavedItem(listId, movieId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            isVisible = true,
                            selectedItemId = movieId,
                            selectedListId = null,
                        ),
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
                        isVisible = false,
                    ),
            )
        }
    }

    override fun onSnackBarActionClick() {
        getGenres()
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
            dispatcher = defaultDispatcher,
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
        )
    }

    private fun onAddItemToListSuccess() {
        onSaveToListBottomSheetDismiss()
        showItemSavedSuccessfullySnackBar()
    }

    private fun showItemSavedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun onAddItemToListStart() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onAddItemToListFinished() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    override fun onCreateNewListClicked() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isVisible = true,
                    ),
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        isVisible = false,
                    ),
            )
        }
    }

    override fun onLoginClicked() {
        sendEffect(
            ContinueWatchingScreenEffect.NavigateToLogin,
        )
    }

    override fun onSaveToListBottomSheetDismiss() {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    AddToListBottomSheetState(
                        savedLists = it.addToListBottomSheetState.savedLists,
                    ),
            )
        }
    }

    override fun onListSelected(listId: Long) {
        updateState {
            it.copy(
                addToListBottomSheetState =
                    it.addToListBottomSheetState.copy(
                        selectedListId = listId,
                    ),
            )
        }
    }

    override fun onCreatedListNameChanged(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        listName = name,
                    ),
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
                        isVisible = true,
                    ),
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
            onSuccess = { onCreateListSuccess() },
            dispatcher = defaultDispatcher,
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished,
        )
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onCreateListStart() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = true,
                    ),
            )
        }
    }

    private fun onCreateListFinished() {
        updateState {
            it.copy(
                addListBottomSheetState =
                    it.addListBottomSheetState.copy(
                        isLoading = false,
                    ),
            )
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
