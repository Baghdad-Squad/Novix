package com.baghdad.viewmodel.movie

import androidx.paging.map
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.flow.map

class TrendingMoviesViewModel(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel<TrendingMoviesScreenState, TrendingMoviesEffect>(TrendingMoviesScreenState()),
    TrendingMoviesInteractionListener {

    init {
        loadGenres()
        loadMoviesByGenres(0L)
    }

    private fun loadGenres() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::handleGenreSuccess,
            onFinally = ::stopLoading
        )
    }

    private fun loadMoviesByGenres(categoryId: Long) {
        collectPagingFlow(
            loadData = { page ->
                getTrendingMoviesUseCase(page = page, genreId = categoryId)
            },
            onInitialLoadFinished = ::stopLoading,
            mapEntityToUiState = { it.toMovieUiState() },
            onFlowCreated = { flow ->
                updateState {
                    it.copy(
                        movies = flow,
                        isLoading = true
                    )
                }
            }
        )
    }

    private fun handleGenreSuccess(genres: List<Genre>) {
        val allCategory = TrendingMoviesScreenState.TrendingCategoryUiState(
            id = 0L,
            name = "All",
            isSelected = true
        )

        val categoryList = genres.map {
            TrendingMoviesScreenState.TrendingCategoryUiState(
                id = it.id,
                name = it.name,
                isSelected = false
            )
        }

        val finalCategories = listOf(allCategory) + categoryList

        updateState { it.copy(categories = finalCategories) }

    }

    override fun onBackClick() {
        sendEffect(TrendingMoviesEffect.NavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(TrendingMoviesEffect.NavigateToDetails(movieId))
    }

    override fun onToggleSaveMovie(movieId: Long) {
        val currentFlow = uiState.value.movies

        val newFlow = currentFlow.map { pagingData ->
            pagingData.map { movieUiState ->
                if (movieUiState.id == movieId) {
                    movieUiState.copy(isSaved = !movieUiState.isSaved)
                } else {
                    movieUiState
                }
            }
        }

        updateState { it.copy(movies = newFlow) }
    }

    override fun onCategoryClick(categoryId: Long) {
        if (uiState.value.categories.find { it.id == categoryId }?.isSelected == true) return

        updateState {
            it.copy(
                categories = it.categories.map { item ->
                    item.copy(isSelected = item.id == categoryId)
                },
                isLoading = true
            )
        }
        loadMoviesByGenres(categoryId)
    }

    private fun stopLoading() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}
