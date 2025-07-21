package com.baghdad.viewmodel.login

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class LoginViewModel(initialState: LoginUiState) : BaseViewModel<LoginUiState, LoginUiEffect>(
    initialState
), LoginInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        //TODO("Not yet implemented")
    }

    override fun onLoginClicked(userName: String, password: String) {
        //TODO("Not yet implemented")
    }

    override fun onRegisterClicked() {
        //TODO("Not yet implemented")
    }

    override fun onForgotPasswordClicked() {
        //TODO("Not yet implemented")
    }

    override fun onNavigateBackClicked() {
        //TODO("Not yet implemented")
    }

}