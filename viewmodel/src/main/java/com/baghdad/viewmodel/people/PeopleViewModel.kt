package com.baghdad.viewmodel.people

import com.baghdad.domain.usecase.people.GetPopularPeopleUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class PeopleViewModel(
    private val getPeopleUseCase: GetPopularPeopleUseCase
) : BaseViewModel<PeopleUiState, PeopleUiEffect>(PeopleUiState()),
    PeopleInteractionListener {

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
            mapEntityToUiState = { it.toPeopleUi() },
            onFlowCreated = { flow ->
                updateState { it.copy(people = flow) }
            }
        )
    }

    override fun onBackClick() {
        sendEffect(PeopleUiEffect.OnBackClick)
    }

    override fun onPeopleClick(peopleId: Long) {
        sendEffect(PeopleUiEffect.NavigateToActorDetails(peopleId))
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}
