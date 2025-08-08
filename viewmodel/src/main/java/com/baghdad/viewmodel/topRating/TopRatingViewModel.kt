package com.baghdad.viewmodel.topRating

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.SavedListUiState
import com.baghdad.viewmodel.shared.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopRatingViewModel @Inject constructor(
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
) : BaseViewModel<TopRatingState, TopRatingEffect>(TopRatingState()),
    TopRatingInteractionListener {
    init {
        loadInitData()
        checkIfUserIsLoggedIn()
    }

    private fun loadInitData() {
        getMovieGenres()
        fetchMoviesByGenre(null)
    }

    private fun getMovieGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::onGenresFetched,
            onError = ::onError,
            onStart = ::onStart,
            onFinally = ::onFinally,
        )
    }

    private fun getTvShowGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getTvShowGenres() },
            onSuccess = ::onGenresFetched,
            onError = ::onError,
            onStart = ::onStart,
            onFinally = ::onFinally,
        )
    }

    private fun onGenresFetched(genres: List<Genre>) {
        updateState {
            it.copy(
                genres =
                    genres.distinctBy { genre -> genre.id }.map { genre ->
                        genre.toTopRatingGenreUiState()
                    }
            )
        }

    }

    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopRatingEffect.NavigateToMovieDetails(movieId))
    }

    override fun onTvShowDetailsClick(tvShowId: Long) {
        sendEffect(TopRatingEffect.NavigateToTvShowDetails(tvShowId))
    }

    private fun fetchTvShowsByGenre(genreId: Long?) {
        hideSnackBar()
        updateState { it.copy(isLoading = true, selectedTvShowGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getTvShowTopRatingUseCase.invoke(
                    page = page,
                    genreId = genreId
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingTvShowUiState() },
            onFlowCreated = { tvShowsFlow -> updateState { it.copy(tvShowsFlow = tvShowsFlow) } },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
            onInitialLoadError = ::onError
        )
    }

    private fun fetchMoviesByGenre(genreId: Long?) {
        hideSnackBar()
        updateState { it.copy(isLoading = true, selectedMovieGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getMovieTopRatingUseCase.invoke(
                    genreId = genreId,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingMovieUiState() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } },
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            },
            onInitialLoadError = ::onError
        )
    }

    override fun onGenreClick(genreId: Long?) {
        when (currentState.selectedTab) {
            TopRatingTab.MOVIES -> {
                if (genreId != currentState.selectedMovieGenreId) {
                    updateState { it.copy(selectedMovieGenreId = genreId) }
                    fetchMoviesByGenre(genreId)
                }
            }

            TopRatingTab.TV_SHOWS -> {
                if (genreId != currentState.selectedTvShowGenreId) {
                    updateState { it.copy(selectedTvShowGenreId = genreId) }
                    fetchTvShowsByGenre(genreId)
                }
            }
        }
    }

    private fun onSaveButtonClicked(
        listId: Long,
        itemId: Long,
        isSaved: Boolean,
    ) {
        if (isSaved) {
            removeSavedItem(listId, itemId)
        } else {
            updateState {
                it.copy(
                    addToListBottomSheetState =
                        it.addToListBottomSheetState.copy(
                            isVisible = true,
                            selectedItemId = itemId,
                            selectedListId = null,
                        ),
                )
            }
        }
    }

    override fun onSaveMovieClick() {
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
            onStart = ::onAddItemToListStart,
            onFinally = ::onAddItemToListFinished,
        )
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::onCheckIfUserIsLoggedInSuccess,
        )
    }

    override fun onBackClick() {
        sendEffect(TopRatingEffect.NavigateBack)
    }

    override fun onSelectedTab(selectedTab: TopRatingTab) {
        if (currentState.selectedTab == selectedTab) return

        updateState {
            it.copy(selectedTab = selectedTab)
        }

        when (selectedTab) {
            TopRatingTab.MOVIES -> {
                getMovieGenres()
                fetchMoviesByGenre(currentState.selectedMovieGenreId)
            }

            TopRatingTab.TV_SHOWS -> {
                getTvShowGenres()
                fetchTvShowsByGenre(currentState.selectedTvShowGenreId)
            }
        }
    }

    private fun showItemRemovedSuccessfullySnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true,
        )
    }

    private fun removeSavedItem(listId: Long, itemId: Long) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(listId = listId, movieId = itemId) },
            onSuccess = { onRemoveSavedItemSuccess() },
            onFinally = ::onRemoveSavedItemFinished,
        )
    }

    private fun onRemoveSavedItemSuccess() {
        showItemRemovedSuccessfullySnackBar()
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

    private fun onError(throwable: Throwable) {
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

    override fun onSnackBarActionLabelClick() {
        if (currentState.selectedTab == TopRatingTab.MOVIES) {
            getMovieGenres()
            fetchMoviesByGenre(currentState.selectedMovieGenreId)
        } else {
            getTvShowGenres()
            fetchTvShowsByGenre(currentState.selectedTvShowGenreId)
        }
    }

    override fun onCreateNewListClick() {
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

    override fun onLoginClick() {
        sendEffect(TopRatingEffect.NavigateToLogin)
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
            onStart = ::onCreateListStart,
            onFinally = ::onCreateListFinished,
        )
    }

    override fun onTopRatingItemSaveClick(item: TopRatingState.MovieUiState) {
        onSaveButtonClicked(item.savedListId, item.id, item.isSaved)
    }

    private fun onCreateListSuccess() {
        onCreateListBottomSheetDismiss()
        getUserSavedLists()
    }

    private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
        updateState {
            it.copy(isUserLoggedIn = isLoggedIn)
        }
        if (isLoggedIn) {
            getUserSavedLists()
        }
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

    private fun getUserSavedLists() {
        collectPagingFlow(
            loadData = { page ->
                getSavedListsUseCase(
                    page = page,
                    pageSize = 20,
                )
            },
            onInitialLoadError = ::onError,
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

    private fun onStart() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

}

