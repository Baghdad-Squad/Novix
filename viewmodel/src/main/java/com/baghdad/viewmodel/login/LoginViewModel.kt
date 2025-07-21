package com.baghdad.viewmodel.login

import android.util.Log
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class LoginViewModel : BaseViewModel<LoginUiState, LoginUiEffect>(
    LoginUiState()
), LoginInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
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

    override fun onPasswordValueChange(value: String) {
        isAnyFieldEmpty()
        updateState {
            Log.i("TAG", "password: $value")
            it.copy(password = value)
        }
    }

    override fun onUserNameValueChange(value: String) {
        isAnyFieldEmpty()
        updateState {
            Log.i("TAG", "user name: $value")
            it.copy(userName = value)
        }
    }

    override fun togglePasswordVisibility() {
        updateState {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    private fun isAnyFieldEmpty() {
        updateState {
            it.copy(isAnyFieldEmpty = it.userName.isEmpty() || it.password.isEmpty())
        }
    }

}