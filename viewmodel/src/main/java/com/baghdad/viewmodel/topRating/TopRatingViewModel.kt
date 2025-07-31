package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.topRated.GetMovieTopRatingUseCase
import com.baghdad.domain.usecase.topRated.GetTvShowTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatingViewModel @Inject constructor(
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getTvShowTopRatingUseCase: GetTvShowTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TopRatingState, TopRatingEffect>(TopRatingState()),
    TopRatingInteractionListener {
    init {
        getMovieGenres()
        fetchMoviesByGenre(null)
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
            onFlowCreated = { tvShowsFlow -> updateState { it.copy(tvShowsFlow = tvShowsFlow) } }
        )
    }

    private fun fetchMoviesByGenre(genreId: Long?) {
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
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } }
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

    override fun onSaveTvShowClick(tvShowId: Long) {

        showSnackBar(
            message = SearchSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
        )
    }

    override fun onSaveMovieClick(movieId: Long) {

        showSnackBar(
            message = SearchSnackBarMessage.SavedItemSuccessfully,
            isSuccess = true,
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
                fetchMoviesByGenre(currentState.selectedMovieGenreId)
            }

            TopRatingTab.TV_SHOWS -> {
                getTvShowGenres()
                fetchTvShowsByGenre(currentState.selectedTvShowGenreId)
            }
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

}
