package com.baghdad.viewmodel.onBoarding

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.usecase.onBoarding.SetFirstTimeLaunchAppUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val setFirstTimeLaunchApp: SetFirstTimeLaunchAppUseCase,
    private val ioDispatcher: CoroutineDispatcher,
): BaseViewModel<OnBoardingState, OnBoardingEffect>(OnBoardingState()),
    OnBoardingInteractionListener {


    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onNextButtonClick(sizeOfBoardingInfo: Int) {
        tryToExecute(
            callee = {
                if (currentState.currentPage < sizeOfBoardingInfo) {
                    updateState { it.copy(currentPage = currentState.currentPage + 1) }
                }
            },
            onError = {
                LocalDataBaseException()
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
            onError = {
                LocalDataBaseException()
            },
            dispatcher = ioDispatcher
        )
    }


}
