package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.movie.GetTrendingMoviesUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<TrendingMoviesScreenState, TrendingMoviesEffect>(TrendingMoviesScreenState()),
    TrendingMoviesInteractionListener {
    init {
        loadGenres()
        loadMoviesByGenres(null)
    }


    private fun loadGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getMovieGenres() },
            onSuccess = ::handleGenreSuccess,
            onError = ::onLoadDataError,
            dispatcher = ioDispatcher
        )
    }

    private fun loadMoviesByGenres(categoryId: Long?) {
        updateState { it.copy(isLoading = true, selectedGenreId = categoryId) }
        collectPagingFlow(
            loadData = { page ->
                getTrendingMoviesUseCase.invoke(
                    genreId = currentState.selectedGenreId,
                    page = page,
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toMovieUiState() },
            onFlowCreated = { flow ->
                updateState {
                    it.copy(
                        movies = flow,
                    )
                }
                hideSnackBar()
            },
            onInitialLoadError = ::onLoadDataError
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
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

    private fun handleGenreSuccess(genres: List<Genre>) {
        val categoryList =
            genres.map(Genre::toGenreUiState)

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
            loadMoviesByGenres(categoryId)
        }
    }

    override fun onSnackBarActionLabelClick(categoryId: Long?) {
        loadGenres()
        loadMoviesByGenres(categoryId)
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}