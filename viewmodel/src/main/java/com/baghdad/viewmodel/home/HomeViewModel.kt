package com.baghdad.viewmodel.home

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.ObserveContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.movie.GetPopularMoviesUseCase
import com.baghdad.domain.usecase.movie.GetUpcomingMoviesUseCase
import com.baghdad.domain.usecase.savedList.AddMovieToSavedListUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.tvShow.GetPopularTvShowsUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
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
class HomeViewModel
    @Inject
    constructor(
        private val getGenresUseCase: GetGenresUseCase,
        private val observeContinueWatchingUseCase: ObserveContinueWatchingUseCase,
        private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
        private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
        private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
        private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
        private val isUserLoggedInUseCase: IsLoggedInUseCase,
        private val getSavedListsUseCase: GetSavedListsUseCase,
        private val addMovieToSavedListUseCase: AddMovieToSavedListUseCase,
        private val createSavedListUseCase: CreateSavedListUseCase,
        private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
        private val defaultDispatcher: CoroutineDispatcher,
    ) : BaseViewModel<HomeScreenState, HomeScreenEffect>(HomeScreenState()),
        HomeInteractionListener {
        init {
            loadData()
        }

        private fun loadData() {
            checkIfUserIsLoggedIn()
            getPopularItems()
            getTopRatingMovies()
            observeContinueWatchingItems()
            getMovieGenres()
            getUpcomingItems()
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
                onInitialLoadError = ::onLoadDataError,
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

        private fun getPopularItems() {
            tryToExecute(
                callee = { getPopularMoviesUseCase() to getPopularTvShowsUseCase() },
                dispatcher = defaultDispatcher,
                onSuccess = ::onGetPopularItemsSuccess,
                onStart = ::onGetPopularItemsStart,
                onFinally = ::onGetPopularItemsFinished,
                onError = ::onLoadDataError,
            )
        }

        private fun onGetPopularItemsSuccess(popularItems: Pair<List<Movie>, List<TvShow>>) {
            val (movies, tvShows) = popularItems
            val popularMovies = movies.take(POPULAR_MOVIES_LIMIT).map(Movie::toPopularItemUiState)
            val popularTvShows = tvShows.take(POPULAR_TV_SHOWS_LIMIT).map(TvShow::toPopularItemUiState)
            updateState {
                it.copy(
                    popularItems = (popularMovies + popularTvShows).shuffled(),
                )
            }
        }

        private fun onLoadDataError(throwable: Throwable) {
            when (throwable) {
                is NoInternetException -> showNoInternetSnackBar()
                else -> handleError(throwable)
            }
        }

        private fun onGetPopularItemsStart() {
            updateState {
                it.copy(isPopularLoading = true)
            }
        }

        private fun onGetPopularItemsFinished() {
            updateState {
                it.copy(isPopularLoading = false)
            }
        }

        private fun getTopRatingMovies() {
            tryToExecute(
                callee = { getMovieTopRatingUseCase(DEFAULT_PAGE, null).data },
                dispatcher = defaultDispatcher,
                onSuccess = ::onGetTopRatingMoviesSuccess,
                onStart = ::onGetTopRatingMoviesStart,
                onFinally = ::onGetTopRatingMoviesFinished,
                onError = ::onLoadDataError,
            )
        }

        private fun onGetTopRatingMoviesSuccess(movies: List<Movie>) {
            updateState {
                it.copy(
                    topRatingItems =
                        movies
                            .take(TOP_RATING_MOVIES_LIMIT)
                            .map(Movie::toTopRatingItemUiState),
                )
            }
        }

        private fun onGetTopRatingMoviesStart() {
            updateState {
                it.copy(isTopRatingLoading = true)
            }
        }

        private fun onGetTopRatingMoviesFinished() {
            updateState {
                it.copy(isTopRatingLoading = false)
            }
        }

        private fun observeContinueWatchingItems() {
            tryToCollect(
                flowProvider = observeContinueWatchingUseCase::invoke,
                dispatcher = defaultDispatcher,
                onNewValue = ::onNewContinueWatchingItems,
                onError = ::onLoadDataError,
            )
        }

        private fun onNewContinueWatchingItems(items: List<ContinueWatching>) {
            updateState {
                it.copy(
                    continueWatchingItems =
                        items
                            .take(CONTINUE_WATCHING_LIMIT)
                            .map(ContinueWatching::toUiState),
                    isContinueWatchingLoading = false,
                )
            }
        }

        private fun getMovieGenres() {
            tryToExecute(
                callee = getGenresUseCase::getMovieGenres,
                dispatcher = defaultDispatcher,
                onSuccess = ::onGetMovieGenresSuccess,
                onStart = ::onGetMovieGenresStart,
                onFinally = ::onGetMovieGenresFinished,
                onError = ::onLoadDataError,
            )
        }

        private fun onGetMovieGenresSuccess(genres: List<Genre>) {
            updateState {
                it.copy(upcomingGenres = genres.map(Genre::toUiState))
            }
        }

        private fun onGetMovieGenresStart() {
            updateState {
                it.copy(isUpcomingGenresLoading = true)
            }
        }

        private fun onGetMovieGenresFinished() {
            updateState {
                it.copy(isUpcomingGenresLoading = false)
            }
        }

        private fun getUpcomingItems() {
            tryToExecute(
                callee = { getUpcomingMoviesUseCase(currentState.selectedUpcomingGenreId) },
                dispatcher = defaultDispatcher,
                onSuccess = ::onGetUpcomingSuccess,
                onStart = ::onGetUpcomingStarted,
                onFinally = ::onGetUpcomingFinished,
                onError = ::onLoadDataError,
            )
        }

        private fun onGetUpcomingSuccess(movies: List<Movie>) {
            hideSnackBar()
            updateState {
                it.copy(upcomingItems = movies.map(Movie::toUpcomingItemUiState))
            }
        }

        private fun onGetUpcomingStarted() {
            updateState {
                it.copy(isUpcomingMoviesLoading = true)
            }
        }

        private fun onGetUpcomingFinished() {
            updateState {
                it.copy(isUpcomingMoviesLoading = false)
            }
        }

        override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.UnknownError

        override fun onPopularItemClicked(item: HomeScreenState.PopularItemUiState) {
            if (item.type == HomeScreenState.PopularItemUiState.Type.MOVIE) {
                sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
            } else {
                sendEffect(HomeScreenEffect.NavigateToTvShowDetails(item.id))
            }
        }

        override fun onPopularItemSaveClicked(item: HomeScreenState.PopularItemUiState) {
            onSaveButtonClicked(item.savedListId, item.id, item.isSaved)
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

        override fun onMoviesClicked() {
            sendEffect(HomeScreenEffect.NavigateToMovies)
        }

        override fun onTvShowsClicked() {
            sendEffect(HomeScreenEffect.NavigateToTvShows)
        }

        override fun onActorsClicked() {
            sendEffect(HomeScreenEffect.NavigateToActors)
        }

        override fun onTopRatingItemClicked(item: HomeScreenState.TopRatingItemUiState) {
            sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
        }

        override fun onTopRatingItemSaveClicked(item: HomeScreenState.TopRatingItemUiState) {
            onSaveButtonClicked(item.savedListId, item.id, item.isSaved)
        }

        override fun onViewAllTopRatingClicked() {
            sendEffect(HomeScreenEffect.NavigateToTopRating)
        }

        override fun onContinueWatchingItemClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
            sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
        }

        override fun onContinueWatchingItemSaveClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
            onSaveButtonClicked(item.savedListId, item.id, item.isSaved)
        }

        override fun onViewAllContinueWatchingClicked() {
            sendEffect(HomeScreenEffect.NavigateToContinueWatching)
        }

        override fun onUpcomingGenreSelected(genre: HomeScreenState.GenreUiState?) {
            if (genre?.id != currentState.selectedUpcomingGenreId) {
                updateState {
                    it.copy(selectedUpcomingGenreId = genre?.id)
                }
                getUpcomingItems()
            }
        }

        override fun onUpcomingItemClicked(item: HomeScreenState.UpcomingItemUiState) {
            sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
        }

        override fun onUpcomingItemSaveClicked(item: HomeScreenState.UpcomingItemUiState) {
            onSaveButtonClicked(item.savedListId, item.id, item.isSaved)
        }

        override fun onSnackBarActionLabelClicked() {
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
                HomeScreenEffect.NavigateToLogin,
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

        private fun showNoInternetSnackBar() {
            showSnackBar(
                message = BaseSnackBarMessage.NetworkError,
                actionLabelRes = R.string.retry,
                isSuccess = false,
                durationMillis = Int.MAX_VALUE.toLong(),
            )
        }

        companion object {
        private const val POPULAR_MOVIES_LIMIT = 5
        private const val POPULAR_TV_SHOWS_LIMIT = 5
        private const val TOP_RATING_MOVIES_LIMIT = 10
        private const val CONTINUE_WATCHING_LIMIT = 10
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
