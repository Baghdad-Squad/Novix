package com.baghdad.viewmodel.login

import com.baghdad.domain.exception.InValidUserCredentialException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.usecase.login.LoginUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<LoginUiState, LoginUiEffect>(
    LoginUiState()
), LoginInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onLoginClicked() {
        if (uiState.value.userName.length in 4..32 && uiState.value.password.length >= 4) {
            tryToExecute(
                onStart = { startLoading() },
                callee = {
                    loginUseCase.invoke(
                        userName = uiState.value.userName,
                        password = uiState.value.password
                    )
                },
                dispatcher = ioDispatcher,
                onSuccess = { onLoginSuccess() },
                onError = { onLoginError(it) },
                onFinally = { endLoading() })
        } else {
            showSnackBar(
                message = BaseSnackBarMessage.InvalidCredential, isSuccess = false
            )
        }
    }


    fun onLoginSuccess() {
        showSnackBar(
            message = BaseSnackBarMessage.LoginSuccessfully, isSuccess = true
        )
        sendEffect(LoginUiEffect.NavigateToHome)
    }

    fun onLoginError(t: Throwable) {
        when (t) {
            is InValidUserCredentialException -> showSnackBar(
                message = BaseSnackBarMessage.InvalidCredential, isSuccess = false
            )

            is NoInternetException -> showSnackBar(
                message = BaseSnackBarMessage.NoInternetException, isSuccess = false
            )

            is UnKnownNetworkException -> showSnackBar(
                message = BaseSnackBarMessage.NetworkError, isSuccess = false
            )

            else -> showSnackBar(
                message = BaseSnackBarMessage.UnknownError, isSuccess = false
            )
        }
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
        sendEffect(LoginUiEffect.NavigateBack)
    }

    override fun onPasswordValueChange(value: String) {
        if (value.length < 20) {
            updateState {
                it.copy(password = value)
            }
        }

        isAnyFieldEmpty()

    }

    override fun onUserNameValueChange(value: String) {
        if (value.length < 20) {
            updateState {
                it.copy(userName = value)
            }
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