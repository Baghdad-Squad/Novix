package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages

class TopRatingViewModel(
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TopRatingState, TopRatingEffect>(TopRatingState()),
    TopRatingInteractionListener {

    init {
        getMovieGenres()
        fetchMoviesByGenre(0L)
    }

    private fun getMovieGenres() {
        tryToExecute(
            { getGenresUseCase.getMovieGenres() },
            ::onGenresFetched,
        )
    }

    private fun getTvShowGenres() {
        tryToExecute(
            { getGenresUseCase.getTvShowGenres() },
            ::onGenresFetched,
        )
    }

    private fun onGenresFetched(
        genres: List<Genre>
    ) {
        updateState {
            it.copy(
                genres = listOf(TopRatingState.GenreUiState(name = "All")) + genres
                    .distinctBy { genre -> genre.id }
                    .map { genre -> genre.toTopRatingGenreUiState() }
            )
        }

    }


    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopRatingEffect.NavigateToMovieDetails(movieId))
    }

    private fun fetchTvShowsByGenre(genreId: Long) {
        updateState { it.copy(isLoading = true, selectedGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getTvShowTopRatingUseCase.invoke(
                    page = page,
                    genreId = genreId
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingTvShowUiState() },
            onFlowCreated = { tvShowsFlow -> updateState { it.copy(tvShowsFlow = tvShowsFlow) } }
        )
    }

    private fun fetchMoviesByGenre(genreId: Long) {
        updateState { it.copy(isLoading = true, selectedGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getMovieTopRatingUseCase.invoke(
                    genreId = genreId,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingMovieUiState() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } }
        )
    }

    override fun onGenreClick(genreId: Long) {
        when (currentState.selectedTab) {
            TopRatingTab.MOVIES -> fetchMoviesByGenre(genreId)
            TopRatingTab.TV_SHOWS -> fetchTvShowsByGenre(genreId)
        }
    }


    override fun onSaveMovieClick(movieId: Long) {
        showSnackBar(
            message = SearchScreenBaseSnackBarMessages.SavedItemSuccessfully, isSuccess = true
        )
    }

    override fun onBackClick() {
        sendEffect(TopRatingEffect.NavigateBack)
    }

    override fun onSelectedTab(selectedTab: TopRatingTab) {
        updateState {
            it.copy(selectedTab = selectedTab)
        }
        when (selectedTab) {
            TopRatingTab.MOVIES -> {

                getMovieGenres()

                fetchMoviesByGenre(currentState.selectedGenreId)
            }

            TopRatingTab.TV_SHOWS -> {
                getTvShowGenres()
                fetchTvShowsByGenre(currentState.selectedGenreId)
            }
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}
