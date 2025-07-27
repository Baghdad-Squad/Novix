package com.baghdad.viewmodel.login

import com.baghdad.domain.usecase.login.LoginUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginUiState, LoginUiEffect>(
    LoginUiState()
), LoginInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onLoginClicked() {
        tryToExecute(
            onStart = { startLoading() },
            callee = {
                loginUseCase.invoke(
                    userName = uiState.value.userName,
                    password = uiState.value.password
                )
            },
            onSuccess = { onLoginSuccess() },
            onError = { onLoginError(it) },
            onFinally = { endLoading() }
        )
    }

    fun onLoginSuccess() {
        showSnackBar(
            message = BaseSnackBarMessage.LoginSuccessfully,
            isSuccess = true
        )
        sendEffect(LoginUiEffect.NavigateToHome)
    }

    fun onLoginError(t: Throwable) {
        showSnackBar(
            message = BaseSnackBarMessage.InvalidCredential,
            isSuccess = false
        )
    }

    override fun onRegisterClicked() {
        sendEffect(
            LoginUiEffect.NavigateToRegister
        )
    }

    override fun onForgotPasswordClicked() {
        sendEffect(LoginUiEffect.NavigateToForgotPassword)
    }

    override fun onNavigateBackClicked() {
//        sendEffect(LoginUiEffect.NavigateBack)
    }

    override fun onPasswordValueChange(value: String) {
        updateState {
            it.copy(password = value)
        }
        isAnyFieldEmpty()

    }

    override fun onUserNameValueChange(value: String) {
        updateState {
            it.copy(userName = value)
        }
        isAnyFieldEmpty()
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

    private fun startLoading() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun endLoading() {
        updateState {
            it.copy(
                isLoading = false
            )
        }
    }

}