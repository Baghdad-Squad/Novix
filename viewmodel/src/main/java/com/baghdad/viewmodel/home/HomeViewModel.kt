package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.usecase.continueWatching.ObserveContinueWatchingUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetPopularMoviesUseCase
import com.baghdad.domain.usecase.movie.GetUpcomingMoviesUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.tvShow.GetPopularTvShowsUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class HomeViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val observeContinueWatchingUseCase: ObserveContinueWatchingUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) : BaseViewModel<HomeScreenState, HomeScreenEffect>(HomeScreenState()),
    HomeInteractionListener {
    init {
        getPopularItems()
        getTopRatingMovies()
        observeContinueWatchingItems()
        getMovieGenres()
        collectPaginatedUpcomingMovies()
    }

    private fun getPopularItems() {
        tryToExecute(
            callee = { getPopularMoviesUseCase() to getPopularTvShowsUseCase() },
            onSuccess = ::onGetPopularItemsSuccess,
            onStart = ::onGetPopularItemsStart,
            onFinally = ::onGetPopularItemsFinished,
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
            onSuccess = ::onGetTopRatingMoviesSuccess,
            onStart = ::onGetTopRatingMoviesStart,
            onFinally = ::onGetTopRatingMoviesFinished,
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
            onNewValue = ::onNewContinueWatchingItems,
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
            onSuccess = ::onGetMovieGenresSuccess,
            onStart = ::onGetMovieGenresStart,
            onFinally = ::onGetMovieGenresFinished,
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

    private fun collectPaginatedUpcomingMovies() {
        updateState {
            it.copy(isUpcomingMoviesLoading = true)
        }
        collectPagingFlow(
            loadData = { page ->
                getUpcomingMoviesUseCase(
                    page,
                    currentState.selectedUpcomingGenreId,
                )
            },
            onInitialLoadFinished = {
                updateState {
                    it.copy(isUpcomingMoviesLoading = false)
                }
            },
            pageSize = UPCOMING_PAGE_SIZE,
            mapEntityToUiState = Movie::toUpcomingItemUiState,
            onFlowCreated = { flow ->
                updateState {
                    it.copy(upcomingItems = flow)
                }
            },
        )
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onPopularItemClicked(item: HomeScreenState.PopularItemUiState) {
        if (item.type == HomeScreenState.PopularItemUiState.Type.MOVIE) {
            sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
        } else {
            sendEffect(HomeScreenEffect.NavigateToTvShowDetails(item.id))
        }
    }

    override fun onPopularItemSaveClicked(item: HomeScreenState.PopularItemUiState) {
//        TODO("Implement when saving lists is implemented")
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
//        TODO("Implement when saving lists is implemented")
    }

    override fun onViewAllTopRatingClicked() {
        sendEffect(HomeScreenEffect.NavigateToTopRating)
    }

    override fun onContinueWatchingItemClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
        sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
    }

    override fun onContinueWatchingItemSaveClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
//        TODO("Implement when saving lists is implemented")
    }

    override fun onViewAllContinueWatchingClicked() {
        sendEffect(HomeScreenEffect.NavigateToContinueWatching)
    }

    override fun onUpcomingGenreSelected(genre: HomeScreenState.GenreUiState?) {
        if (genre?.id != currentState.selectedUpcomingGenreId) {
            updateState {
                it.copy(selectedUpcomingGenreId = genre?.id)
            }
            collectPaginatedUpcomingMovies()
        }
    }

    override fun onUpcomingItemClicked(item: HomeScreenState.UpcomingItemUiState) {
        sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
    }

    override fun onUpcomingItemSaveClicked(item: HomeScreenState.UpcomingItemUiState) {
//        TODO("Implement when saving lists is implemented")
    }

    companion object {
        private const val POPULAR_MOVIES_LIMIT = 5
        private const val POPULAR_TV_SHOWS_LIMIT = 5
        private const val TOP_RATING_MOVIES_LIMIT = 10
        private const val CONTINUE_WATCHING_LIMIT = 10
        private const val DEFAULT_PAGE = 1
        private const val UPCOMING_PAGE_SIZE = 1
    }
}
