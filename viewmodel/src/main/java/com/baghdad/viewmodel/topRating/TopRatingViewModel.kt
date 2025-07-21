package com.baghdad.viewmodel.topRating

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetMovieTopRatingUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchScreenBaseSnackBarMessages
import com.baghdad.viewmodel.topRating.TopRatingMovieState.GenreUiState
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class TopRatingViewModel(
    val genreId: Long,
    private val getMovieTopRatingUseCase: GetMovieTopRatingUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TopRatingMovieState, TopRatingEffect>(TopRatingMovieState()),
    TopRatingInteractionListener {

    init {
        getMovieGenres()
        getTopRatedMoviesByGenre()
    }

    private fun getMovieGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = { genres ->
                updateState { state ->
                    val genreUiStates = genres.map { it.toTopRatingUIState() }
                    state.copy(
                        genres = genreUiStates,
                        moviesByGenreFilter = state.moviesByGenreFilter.copy(
                            moviesFilter = state.moviesByGenreFilter.moviesFilter.copy(
                                allGenres = genreUiStates
                            )
                        )
                    )
                }
            },
            onError = { mapThrowableToErrorMessage(it) }
        )
    }

    private fun getTopRatedMoviesByGenre() {
//        tryToExecute(
//            callee = {
//                getMovieTopRatingUseCase.invoke(
//                    filter = currentState.moviesByGenreFilter.moviesFilter.copyForTopRating(),
//                    page = 1
//                )
//            },
//            onSuccess = { onGetTopRatedMoviesSuccess(it.data) },
//            onError = { mapThrowableToErrorMessage(it) }
//        )
        collectPagingFlow(
            loadData = { page ->
                getMovieTopRatingUseCase.invoke(
                    filter = currentState.moviesByGenreFilter.moviesFilter.copyForTopRating(),
                    page = page
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = {it.toTopRatingUIState()},
            onFlowCreated = {moviesFlow -> updateState { it.copy(movies = moviesFlow) }}
        )
    }

//    private fun onGetTopRatedMoviesSuccess(movies: List<Movie>) {
//        val movieUiStates = movies.map { it.toTopRatingUIState() }
//
//        updateState {
//            it.copy(
//                allTopRatedMovies = movieUiStates,
//                movies = movieUiStates
//            )
//        }
//    }

    override fun onMovieDetailsClick(movieId: Long) {
        sendEffect(TopRatingEffect.NavigateToMovieDetails(movieId))
    }

    override fun onGenreClick(genreId: Long) {
        val genre = currentState.genres.find { it.id == genreId } ?: return
        val currentSelected = currentState.moviesByGenreFilter.moviesFilter.selectedGenres
        val updatedSelected = if (currentSelected.id == genre.id) GenreUiState() else genre

        updateState {
            it.copy(
                moviesByGenreFilter = it.moviesByGenreFilter.copy(
                    moviesFilter = it.moviesByGenreFilter.moviesFilter.copy(
                        selectedGenres = updatedSelected
                    )
                )
            )
        }
        getTopRatedMoviesByGenre()
    }

    override fun onSaveMovieClick(movieId: Long) {
//        updateState {
//            it.copy(
//                movies = it.movies.map { movie ->
//                    if (movie == movieId) movie.copy(isSaved = movie.isSaved.not()) else movie
//                }
//            )
//        }
//        showSnackBar(
//            message = SearchScreenBaseSnackBarMessages.SavedItemSuccessfully, isSuccess = true
//        )
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
