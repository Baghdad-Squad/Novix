package com.baghdad.viewmodel.categoryMovies

import androidx.paging.PagingData
import androidx.paging.map
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class CategoryMoviesViewModel @Inject constructor(
    private val genreId: Long,
    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase,
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
                moviesFlow = it.moviesFlow.map { pagingData ->
                    pagingData.map { movie ->
                        if (movie.id == movieId) {
                            movie.copy(isSaved = !movie.isSaved)
                        } else {
                            movie
                        }
                    }
                }
            )
        }
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(CategoryMoviesEffect.NavigateToMovieDetails(movieId))
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
        collectPagingFlow(
            loadData = { page ->
                getGenreMoviesUseCase(genreId, page)
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onGetGenreMoviesSuccess

        )

    }

    private fun onGetGenreMoviesSuccess(moviesFlow: Flow<PagingData<CategoryMoviesState.MovieUiState>>) {
        updateState { it.copy(moviesFlow = moviesFlow) }
    }

    private fun onGetGenreMoviesError(throwable: Throwable) {}

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
