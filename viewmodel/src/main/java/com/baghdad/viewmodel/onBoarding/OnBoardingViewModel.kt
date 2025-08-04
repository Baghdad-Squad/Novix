package com.baghdad.viewmodel.onBoarding

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage


class OnBoardingViewModel : BaseViewModel<OnBoardingState, OnBoardingEffect>(OnBoardingState()),
    OnBoardingInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onNextButtonClick() {
        if (currentState.currentPage < currentState.onBoardingInfo.size) {
            updateState { it.copy(currentPage = currentState.currentPage + 1) }
        } else if (currentState.currentPage == currentState.onBoardingInfo.size) {
            sendEffect(OnBoardingEffect.NavigateToWelcomeToNovix)
        }
    }

    override fun onBackButtonClick() {
        if (currentState.currentPage > 0) {
            updateState { it.copy(currentPage = currentState.currentPage - 1) }
        }
    }

    override fun onSkipButtonClick() {
        updateState { it.copy(currentPage = 0) }
        sendEffect(OnBoardingEffect.NavigateToWelcomeToNovix)
    }


}
