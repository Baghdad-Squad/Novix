package com.baghdad.viewmodel.categoryMovies

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetMovieGenreNameByIdUseCase
import com.baghdad.domain.usecase.movie.GetMoviesByGenreUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CategoryMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGenreMoviesUseCase: GetMoviesByGenreUseCase,
    private val getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<CategoryMoviesState, CategoryMoviesEffect>(CategoryMoviesState()),
    CategoryMoviesInteractionListener {

    private val categoryId: Long = checkNotNull(savedStateHandle["categoryId"])
    init {
        loadInitData()
    }

    private fun loadInitData() {
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

    override fun onSnackBarActionLabelClick() {
        loadInitData()
    }

    private fun getGenreName() {
        hideSnackBar()
        tryToExecute(
            callee = { getMovieGenreNameByIdUseCase.invoke(categoryId) },
            onSuccess = { onGetGenreNameSuccess(it.name) },
            onError = { onGetGenreNameError(it) },
            dispatcher = ioDispatcher
        )
    }

    private fun onGetGenreNameSuccess(genreName: String) {
        updateState {
            it.copy(categoryName = genreName)
        }
    }

    private fun onGetGenreNameError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun getGenreMovies() {
        collectPagingFlow(
            loadData = { page ->
                getGenreMoviesUseCase(categoryId, page)
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onGetGenreMoviesSuccess,
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            }

        )

    }

    private fun onGetGenreMoviesSuccess(moviesFlow: Flow<PagingData<CategoryMoviesState.MovieUiState>>) {
        updateState { it.copy(moviesFlow = moviesFlow) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
