package com.baghdad.viewmodel.categoryMovies

import com.baghdad.domain.usecase.genre.GetGenreMoviesUseCase
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class CategoryMoviesViewModel(
    private val genreId: Long,
    private val getGenreMoviesUseCase: GetGenreMoviesUseCase,
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase
) : BaseViewModel<CategoryMoviesState, CategoryMoviesEffect>(CategoryMoviesState()),
    CategoryMoviesInteractionListener {

    init {
        getGenreMovies()
        getGenreName()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onBackClicked() {
        sendEffect(CategoryMoviesEffect.NavigateBack)
    }

    override fun onSavedClick(movieId: Long) {
        updateState {
            it.copy(
                movies = it.movies.map { movie -> if (movie.id == movieId) movie.copy(isSaved = movie.isSaved.not()) else movie }
            )
        }
    }

    override fun onMovieClicked(movieId: Long) {

    }

    private fun getGenreName() {
        tryToExecute(
            callee = { getMovieGenreNameByIdUseCase.invoke(genreId) },
            onSuccess = { onGetGenreNameSuccess(it.name) },
            onError = { onGetGenreNameError(it) }
        )
    }

    private fun onGetGenreNameSuccess(genreName: String) {
        updateState {
            it.copy(categoryName = genreName)
        }
    }

    private fun onGetGenreNameError(throwable: Throwable) {}

    private fun getGenreMovies() {
        tryToExecute(
            callee = { getGenreMoviesUseCase.invoke(genreId) },
            onSuccess = { onGetGenreMoviesSuccess(it) },
            onError = { onGetGenreMoviesError(it) }
        )
    }


    private fun onGetGenreMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(movies = movies.map { it.toUiState() })
        }
    }

    private fun onGetGenreMoviesError(throwable: Throwable) {}


}
