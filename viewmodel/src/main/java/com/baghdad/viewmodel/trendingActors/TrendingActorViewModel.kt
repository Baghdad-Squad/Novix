package com.baghdad.viewmodel.trendingActors

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.actor.GetTrendingActorsUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingActorViewModel @Inject constructor(
    private val getTrendingActorsUseCase: GetTrendingActorsUseCase
) : BaseViewModel<TrendingActorsUiState, TrendingActorsUiEffect>(TrendingActorsUiState()),
    TrendingActorsInteractionListener {

    init {
        getTrendingActors()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    private fun getTrendingActors() {
        collectPagingFlow(
            loadData = { page ->
                getTrendingActorsUseCase(page)
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTrendingActorsUi() },
            onFlowCreated = { flow ->
                updateState { it.copy(trendingActor = flow) }
                hideSnackBar()
            },
            onInitialLoadError = ::onError,
            onLoadingChanged = { isLoading ->
                updateState { it.copy(isLoading = isLoading) }
            }
        )
    }

    override fun onBackClick() {
        sendEffect(TrendingActorsUiEffect.OnBackClick)
    }

    override fun onTrendingActorClick(actorId: Long) {
        sendEffect(TrendingActorsUiEffect.NavigateToActorsDetails(actorId))
    }

    private fun onError(throwable: Throwable) {
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

    override fun onSnackBarActionLabelClick() {
        getTrendingActors()
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
