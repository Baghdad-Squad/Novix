package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.usecase.fake.GetMoviesPageByGenreUseCase
import com.baghdad.domain.usecase.fake.GetTvShowsPageByGenreUseCase
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.flow.flowOf

class ContinueWatchingViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesPageByGenreUseCase, // TODO : these should be replace with ones from local datasource
) : BaseViewModel<ContinueWatchingState, ContinueWatchingScreenEffect>(ContinueWatchingState()),
    ContinueWatchingInteractionListener {
    init {
        getGenres()
        getMovies(0)
    }


    private fun getGenres() {
        tryToExecute(
            { getGenresUseCase.getMovieGenres() + getGenresUseCase.getTvShowGenres() },
            ::onGenresFetched,
        )
    }
    private fun getMovies(genreId: Long) {
        collectPagingFlow(
            { page -> getMoviesByGenreUseCase(genreId, page) },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toContinueWatchingUiState() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow) } }
        )
    }

    private fun onGenresFetched(
        genres: List<Genre>
    ) {
        updateState {
            it.copy(
                genres = listOf(ContinueWatchingState.GenreUiState(name = "All")) + genres
                    .distinctBy { genre -> genre.id }
                    .map { genre -> genre.toContinueWatchingUiState() })
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        sendEffect(ContinueWatchingScreenEffect.NavigateBack)
    }

    override  fun onMovieClick(movieId: Long) {

    }
    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override  fun onTvShowClick(tvShowId: Long) {
        TODO("Not yet implemented")
    }

    override fun onGenreClick(genreId: Long) {
        updateState {
            it.copy(selectedTab = genreId , isLoading = true , moviesFlow = flowOf())
        }
        getMovies(genreId)
    }
}