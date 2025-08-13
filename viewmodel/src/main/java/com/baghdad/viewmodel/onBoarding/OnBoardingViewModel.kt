package com.baghdad.viewmodel.onBoarding

import com.baghdad.domain.usecase.appConfigurations.SetFirstTimeLaunchAppUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setFirstTimeLaunchApp: SetFirstTimeLaunchAppUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<OnBoardingState, OnBoardingEffect>(OnBoardingState()),
    OnBoardingInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onNextButtonClick(sizeOfBoardingInfo: Int) {
        tryToExecute(
            callee = {
                val currentPage = currentState.currentPage
                if (currentPage < sizeOfBoardingInfo - 1) {
                    updateState { it.copy(currentPage = currentPage + 1) }
                } else {
                    onFirstTimeLaunch()
                }
            },
            dispatcher = ioDispatcher
        )
    }

    override fun onBackButtonClick() {
        if (currentState.currentPage > 0) {
            updateState { it.copy(currentPage = currentState.currentPage - 1) }
        }
    }

    override fun onSkipButtonClick() {
        onFirstTimeLaunch()
        sendEffect(OnBoardingEffect.NavigateToWelcomeToNovix)
    }

    private fun onFirstTimeLaunch() {
        tryToExecute(
            callee = {
                setFirstTimeLaunchApp(true)
            },
            onSuccess = { onCompleteOnBoardingSuccess() },
            dispatcher = ioDispatcher
        )
    }

    private fun onCompleteOnBoardingSuccess() {
        sendEffect(OnBoardingEffect.NavigateToWelcomeToNovix)
    }
}
