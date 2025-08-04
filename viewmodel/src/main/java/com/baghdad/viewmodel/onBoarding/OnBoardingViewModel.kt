package com.baghdad.viewmodel.onBoarding

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.usecase.onBoarding.SetFirstTimeLaunchAppUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setFirstTimeLaunchApp: SetFirstTimeLaunchAppUseCase,
): BaseViewModel<OnBoardingState, OnBoardingEffect>(OnBoardingState()),
    OnBoardingInteractionListener {

        init {
            onFirstTimeLaunch()
        }
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onNextButtonClick() {
        tryToExecute(
            callee = {
                if (currentState.currentPage < currentState.onBoardingInfo.size) {
                    updateState { it.copy(currentPage = currentState.currentPage + 1) }
                } else if (currentState.currentPage == currentState.onBoardingInfo.size) {
                    sendEffect(OnBoardingEffect.NavigateToWelcomeToNovix)
                }
            },
            onError = {
                LocalDataBaseException()
            },
        )
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

    private fun onFirstTimeLaunch() {
        tryToExecute(
            callee = {
                setFirstTimeLaunchApp(true)
            },
            onError = {
                LocalDataBaseException()
            }
        )
    }


}
