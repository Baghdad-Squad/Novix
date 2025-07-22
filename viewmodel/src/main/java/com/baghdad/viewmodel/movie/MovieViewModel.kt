package com.baghdad.viewmodel.movie

import android.util.Log
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class MovieViewModel(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel<MovieScreenState, MovieScreenEffect>(MovieScreenState()),
    MovieScreenInteractionListener {

    init {
        loadGenres()
    }


    private fun loadGenres() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::handleGenreSuccess,
            onFinally = ::onFinally
        )
    }


    private fun loadMoviesByGenres(categoryId: Long) {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getTrendingMoviesUseCase(page = 1, genreId = categoryId) },
            onSuccess = ::onGetMoviesSuccess,
            onFinally = ::onFinally
        )
    }

    private fun onGetMoviesSuccess(movies: List<Movie>) {
        val filteredMovies = filterMoviesBySelectedGenre(movies)

        Log.d("MovieViewModel", "Filtered movies: ${filteredMovies.size}")
        Log.d("MovieViewModel", "Movies received: ${movies.size}")

        val uiMovies = filteredMovies.map { it.toMovieUi() }

        updateState { it.copy(movies = uiMovies, isLoading = false) }
    }

    private fun handleGenreSuccess(genres: List<Genre>) {
        val allCategory = MovieScreenState.CategoryUiState(
            id = 0L,
            name = "All",
            isSelected = true
        )

        val categoryList = genres.map {
            MovieScreenState.CategoryUiState(
                id = it.id,
                name = it.name,
                isSelected = false
            )
        }

        val finalCategories = listOf(allCategory) + categoryList

        updateState { it.copy(categories = finalCategories) }

        loadMoviesByGenres(allCategory.id)
    }


    private fun filterMoviesBySelectedGenre(movies: List<Movie>): List<Movie> {
        val selectedCategoryId = uiState.value.categories.find { it.isSelected }?.id

        return if (selectedCategoryId == null || selectedCategoryId == 0L) {
            movies
        } else {
            movies.filter { movie ->
                movie.genres.any { genre -> genre.id == selectedCategoryId }
            }
        }
    }


    override fun onBackClick() {
        sendEffect(MovieScreenEffect.NavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(MovieScreenEffect.NavigateToDetails(movieId))
    }

    override fun onToggleSaveMovie(movieId: Long) {
        updateState {
            it.copy(
                movies = it.movies.map { item ->
                    if (item.id == movieId) item.copy(isSaved = item.isSaved.not()) else item
                }
            )
        }
    }

    override fun onCategoryClick(categoryId: Long) {
        if (uiState.value.categories.find { it.id == categoryId }?.isSelected == true) return

        updateState {
            it.copy(
                categories = it.categories.map { item ->
                    item.copy(isSelected = item.id == categoryId)
                },
                movies = emptyList(),
                isLoading = true
            )
        }

        tryToExecute(
            callee = { getTrendingMoviesUseCase(page = 1, categoryId) },
            onSuccess = ::onGetMoviesSuccess,
            onFinally = ::onFinally
        )
    }


    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}
