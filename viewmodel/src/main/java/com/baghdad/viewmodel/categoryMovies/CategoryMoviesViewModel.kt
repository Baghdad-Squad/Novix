package com.baghdad.viewmodel.categoryMovies

import androidx.paging.PagingData
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.flow.Flow

class CategoryMoviesViewModel(
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
//        TODO("Implement when save feature is available")
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
