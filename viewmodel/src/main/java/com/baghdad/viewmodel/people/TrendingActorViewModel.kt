package com.baghdad.viewmodel.people

import com.baghdad.domain.usecase.actor.GetTrendingActorUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TrendingActorViewModel(
    private val getPeopleUseCase: GetTrendingActorUseCase
) : BaseViewModel<TrendingActorUiState, TrendingActorUiEffect>(TrendingActorUiState()),
    TrendingActorsInteractionListener {

    init {
        getPopularPeople()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    private fun getPopularPeople() {
        collectPagingFlow(
            loadData = { page ->
                getPeopleUseCase(page)
            },
            onInitialLoadFinished = ::onFinally,
            mapEntityToUiState = { it.toTrendingActorsUi() },
            onFlowCreated = { flow ->
                updateState { it.copy(trendingActor = flow) }
            }
        )
    }

    override fun onBackClick() {
        sendEffect(TrendingActorUiEffect.OnBackClick)
    }

    override fun onPeopleClick(peopleId: Long) {
        sendEffect(TrendingActorUiEffect.NavigateToActorDetails(peopleId))
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
