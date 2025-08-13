package com.baghdad.viewmodel.login

import com.baghdad.domain.exception.EmptyFieldException
import com.baghdad.domain.exception.InValidPasswordException
import com.baghdad.domain.exception.InValidUserCredentialException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.usecase.login.LoginUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<LoginUiState, LoginUiEffect>(LoginUiState()), LoginInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onLoginClick() {
        tryToExecute(
            onStart = ::onStart,
            callee = {
                loginUseCase.invoke(
                    userName = uiState.value.userName,
                    password = uiState.value.password
                )
            },
            dispatcher = ioDispatcher,
            onSuccess = { onLoginSuccess() },
            onError = ::onLoginError,
            onFinally = ::onFinally
        )
    }


    fun onLoginSuccess() {
        showSnackBar(message = BaseSnackBarMessage.LoginSuccessfully, isSuccess = true)
        sendEffect(LoginUiEffect.RecreateActivity)
    }

    fun onLoginError(t: Throwable) {
        when (t) {
            is EmptyFieldException -> showSnackBar(
                message = BaseSnackBarMessage.EmptyFieldError, isSuccess = false
            )

            is InValidPasswordException -> showSnackBar(
                message = BaseSnackBarMessage.InValidPasswordError, isSuccess = false
            )

            is InValidUserCredentialException -> showSnackBar(
                message = BaseSnackBarMessage.InValidCredentialsError, isSuccess = false
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

    override fun onRegisterClick() {
        sendEffect(LoginUiEffect.NavigateToRegister)
    }

    override fun onForgotPasswordClick() {
        sendEffect(LoginUiEffect.NavigateToForgotPassword)
    }

    override fun onBackClick() {
        sendEffect(LoginUiEffect.NavigateBack)
    }

    override fun onPasswordValueChange(value: String) {
        updateState {
            it.copy(password = value)
        }
        isAnyFieldEmpty()
    }

    override fun onUserNameValueChange(value: String) {
        updateState {
            it.copy(userName = value.trim())
        }
        isAnyFieldEmpty()
    }

    override fun onTogglePasswordChange() {
        updateState {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    private fun isAnyFieldEmpty() {
        updateState {
            it.copy(
                isAnyFieldEmpty = it.userName.isEmpty() ||
                        it.password.isEmpty()
            )
        }
    }

    private fun onStart() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }
}