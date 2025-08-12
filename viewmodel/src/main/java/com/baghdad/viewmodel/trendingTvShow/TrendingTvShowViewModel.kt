package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.tvShow.GetTrendingTvShowUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowViewModel @Inject constructor(
    private val getTrendingTvShowUseCase: GetTrendingTvShowUseCase,
    private val getTvShowGenresUseCase: GetTvShowGenresUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<TrendingTvShowScreenState, TrendingTvShowScreenEffect>(TrendingTvShowScreenState()),
    TrendingTvShowInteractionListener {

    init {
        getTvShowGenres()
        getTrendingTvShowsByGenre(null)
    }

    private fun getTvShowGenres() {
        tryToExecute(
            callee = { getTvShowGenresUseCase.getTvShowGenres() },
            onSuccess = ::handleGenreSuccess,
            onError = ::onLoadDataError,
            dispatcher = ioDispatcher,
        )
    }

    private fun handleGenreSuccess(genres: List<Genre>) {
        val genreList =
            genres.map(Genre::toUiState)

        updateState { it.copy(genres = genreList) }
    }

    private fun getTrendingTvShowsByGenre(genreId: Long?) {
        updateState { it.copy(isLoading = true, selectedGenreId = genreId) }
        collectPagingFlow(
            loadData = { page ->
                getTrendingTvShowUseCase.invoke(
                    genreId = currentState.selectedGenreId,
                    page = page,
                )
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = { tvShowFlow ->
                updateState { it.copy(trendingTvShows = tvShowFlow) }
                hideSnackBar()
            },
            onInitialLoadError = ::onLoadDataError,
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onTvShowClicked(tvShowId: Long) {
        sendEffect(TrendingTvShowScreenEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onBackIconClicked() {
        sendEffect(TrendingTvShowScreenEffect.NavigateBack)
    }

    override fun onGenreClicked(genreId: Long?) {
        if (genreId != currentState.selectedGenreId) {
            getTrendingTvShowsByGenre(genreId)
        }
    }

    override fun onSnackBarActionLabelClicked(genreId: Long?) {
        hideSnackBar()
        getTvShowGenres()
        getTrendingTvShowsByGenre(genreId)
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
