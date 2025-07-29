package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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
        tryToExecute(
            callee = {
                isLoggedInUseCase.invoke()
            },
            onSuccess = { result ->
                updateState {
                    it.copy(
                        isLoggedIn = result
                    )
                }
            },
            onError = {},
        )
    }

}