package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class MainViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : BaseViewModel<MainState, MainEffect>(
    MainState()
), MainInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    init {
        checkIsLoggedIn()
    }

    override fun checkIsLoggedIn() {
        tryToExecute(callee = {
            isLoggedInUseCase.invoke()
        }, onSuccess = {
            onSuccess(result = it)
        }, onError = {
            onError(it)
        }, onFinally = {
            updateState { it.copy(isLoading = false) }
        })
    }

    private fun onSuccess(result: Boolean) {
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