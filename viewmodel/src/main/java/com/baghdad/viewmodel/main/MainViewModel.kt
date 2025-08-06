package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.onBoarding.IsFirstTimeLaunchAppUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase
) : BaseViewModel<MainState, MainEffect>(
    MainState()
), MainInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    init {
        checkIsFirstTimeUser()
        checkIsLoggedIn()
    }

    override fun checkIsLoggedIn() {
        tryToExecute(callee = {
            isLoggedInUseCase.invoke()
        }, onSuccess = {
            onSuccessLoggedIn(result = it)
        }, onError = {
            onError(it)
        }, onFinally = {
            updateState { it.copy(isLoading = false) }
        })
    }

    override fun checkIsFirstTimeUser() {
        tryToExecute(
            callee = {
                isFirstTimeLaunchAppUseCase()
            },
            onSuccess = ::onSuccessFirstTimeLaunch,
            onError = ::onError
        )

    }
    private fun onSuccessFirstTimeLaunch(isFirstTime: Boolean) {
        updateState {
            it.copy(
                isFirstTimeUser = isFirstTime,
                isLoading = false
            )
        }
    }

    private fun onSuccessLoggedIn(result: Boolean) {
        updateState {
            it.copy(
                isLoggedIn = result,
                isLoading = false,
            )
        }
    }

    private fun onError(throwable: Throwable) {
        updateState {
            it.copy(
                isLoading = false,
            )
        }
    }

}