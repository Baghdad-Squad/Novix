package com.baghdad.viewmodel.movie

import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TrendingMoviesViewModel(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TrendingMoviesScreenState, TrendingMoviesEffect>(TrendingMoviesScreenState()),
    TrendingMoviesInteractionListener {
    init {
        loadGenres()
        loadMoviesByGenres(null)
    }

    private fun loadGenres() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::handleGenreSuccess,
            onFinally = ::stopLoading,
        )
    }

    private fun loadMoviesByGenres(categoryId: Long?) {
        collectPagingFlow(loadData = { page ->
            getTrendingMoviesUseCase(page = page, genreId = categoryId)
        }, onInitialLoadFinished = ::stopLoading, mapEntityToUiState = {
            it.toMovieUiState()
        }, onFlowCreated = { flow ->
            updateState {
                it.copy(
                    movies = flow,
                    isLoading = true,
                )
            }
        })
    }

    private fun handleGenreSuccess(genres: List<Genre>) {
        val categoryList =
            genres.map {
                TrendingMoviesScreenState.TrendingCategoryUiState(
                    id = it.id,
                    name = it.name,
                )
            }

        updateState { it.copy(categories = categoryList) }
    }

    override fun onBackClick() {
        sendEffect(TrendingMoviesEffect.NavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(TrendingMoviesEffect.NavigateToMovieDetails(movieId))
    }

    override fun onToggleSaveMovie(movieId: Long) {
        // TODO: implement logic
    }

    override fun onCategoryClick(categoryId: Long?) {
        if (categoryId != currentState.selectedGenreId) {
            updateState {
                it.copy(
                    selectedGenreId = categoryId,
                )
            }
            loadMoviesByGenres(categoryId)
        }
    }

    fun stopLoading() {
        updateState { it.copy(isLoading = false) }
    }
    public override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.UnknownError
}
