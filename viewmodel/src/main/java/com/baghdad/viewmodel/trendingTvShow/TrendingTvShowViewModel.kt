package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.domain.usecase.tvShow.GetTrendingTvShowUseCase
import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TrendingTvShowViewModel(
    private val getTrendingTvShowUseCase: GetTrendingTvShowUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<TrendingTvShowScreenState, TrendingTvShowScreenEffect>(TrendingTvShowScreenState()),
    TrendingTvShowInteractionListener {
    init {
        loadData()
    }

    private fun loadData() {
        getTvShowGenres()
        getTrendingTvShowsByGenre(null)
    }

    private fun getTvShowGenres() {
        tryToExecute(
            callee = { getGenresUseCase.getTvShowGenres() },
            onSuccess = ::handleGenreSuccess,
            onError = ::onLoadDataError
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
            onError = ::onLoadDataError,
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

    override fun onTvShowClick(tvShowId: Long) {
        sendEffect(TrendingTvShowScreenEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onBackIconClick() {
        sendEffect(TrendingTvShowScreenEffect.NavigateBack)
    }

    override fun onGenreClick(genreId: Long?) {
        if (genreId != currentState.selectedGenreId) {
            getTrendingTvShowsByGenre(genreId)
        }
    }

    override fun onSnackBarActionLabelClick() {
        loadData()
    }

    override fun onSaveTvShowClick(tvShowId: Long) {
//        TODO("Not yet implemented")
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
