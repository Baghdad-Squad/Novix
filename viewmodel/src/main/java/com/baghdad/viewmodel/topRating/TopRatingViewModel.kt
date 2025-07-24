package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetMovieTopRatingUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage

class TopRatingViewModel(
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TopRatingMovieState, TopRatingEffect>(TopRatingMovieState()),
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

    private fun onGenresFetched(
        genres: List<Genre>
    ) {
        updateState {
            it.copy(
                genres = listOf(TopRatingMovieState.GenreUiState(name = "All")) + genres
                    .distinctBy { genre -> genre.id }
                    .map { genre -> genre.toTopRatingGenreUiState() })
        }
    }


    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopRatingEffect.NavigateToMovieDetails(movieId))
    }

    private fun fetchMoviesByGenre(genreId: Long) {
        updateState { it.copy(isLoading = true, selectedGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getMovieTopRatingUseCase.invoke(
                    genreId = currentState.selectedGenreId,
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTopRatingMovieUiState() },
            onFlowCreated = { moviesFlow -> updateState { it.copy(moviesFlow = moviesFlow) } }
        )
    }

    override fun onGenreClick(genreId: Long) {
        fetchMoviesByGenre(genreId)
    }


    override fun onSaveMovieClick(movieId: Long) {
        showSnackBar(
            message = SearchSnackBarMessage.SavedItemSuccessfully, isSuccess = true
        )
    }

    override fun onBackClick() {
        sendEffect(TopRatingEffect.NavigateBack)
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}
