package com.baghdad.viewmodel.login

import app.cash.turbine.test
import com.baghdad.domain.exception.EmptyFieldException
import com.baghdad.domain.exception.InValidPasswordException
import com.baghdad.domain.exception.InValidUserCredentialException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.usecase.login.LoginUseCase
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private var loginUseCase: LoginUseCase = mockk()
    private lateinit var loginViewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    private fun createLoginViewModel(): LoginViewModel {
        return LoginViewModel(loginUseCase, testDispatcher)
    }

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onBackClick should navigate back when clicked`() = runTest {
        loginViewModel = createLoginViewModel()

        loginViewModel.onBackClick()

        loginViewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onUserNameValueChange should update userName and validate fields when called with non-empty value`() {
        loginViewModel = createLoginViewModel()
        val userName = "testuser"

        loginViewModel.onUserNameValueChange(userName)

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
    }

    @Test
    fun `onUserNameValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        loginViewModel = createLoginViewModel()

        loginViewModel.onUserNameValueChange("user")
        loginViewModel.onUserNameValueChange("")

        val state = loginViewModel.uiState.value
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onPasswordValueChange should update password and validate fields when called with non-empty value`() {
        loginViewModel = createLoginViewModel()
        val password = "testpass"

        loginViewModel.onPasswordValueChange(password)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
    }

    @Test
    fun `onPasswordValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        loginViewModel = createLoginViewModel()

        loginViewModel.onPasswordValueChange("pass")
        loginViewModel.onPasswordValueChange("")

        val state = loginViewModel.uiState.value
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `isAnyFieldEmpty should be false when userName field is filled`() {
        loginViewModel = createLoginViewModel()
        val userName = "testuser"

        loginViewModel.onUserNameValueChange(userName)

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
    }

    @Test
    fun `isAnyFieldEmpty should be false when password fields is filled`() {
        loginViewModel = createLoginViewModel()
        val password = "testpass"

        loginViewModel.onPasswordValueChange(password)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
    }

    @Test
    fun `onTogglePasswordChange should toggle isPasswordVisible from false to true when called`() {
        loginViewModel = createLoginViewModel()

        assertThat(loginViewModel.uiState.value.isPasswordVisible).isFalse()

        loginViewModel.onTogglePasswordChange()
        assertThat(loginViewModel.uiState.value.isPasswordVisible).isTrue()
    }

    @Test
    fun `onTogglePasswordChange should toggle isPasswordVisible from true to false when called twice`() =
        runTest {
            loginViewModel = createLoginViewModel()

            loginViewModel.onTogglePasswordChange()
            assertThat(loginViewModel.uiState.value.isPasswordVisible).isTrue()

            loginViewModel.onTogglePasswordChange()
            assertThat(loginViewModel.uiState.value.isPasswordVisible).isFalse()
        }

    @Test
    fun `onRegisterClick should emit NavigateToRegister effect when called`() = runTest {
        loginViewModel = createLoginViewModel()

        loginViewModel.onRegisterClick()

        loginViewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateToRegister)
        }
    }

    @Test
    fun `onForgotPasswordClick should emit NavigateToForgotPassword effect when called`() =
        runTest {
            loginViewModel = createLoginViewModel()

            loginViewModel.onForgotPasswordClick()

            loginViewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect).isEqualTo(LoginUiEffect.NavigateToForgotPassword)
            }
        }


    @Test
    fun `onUserNameValueChange should treat whitespace as valid input when called`() {
        loginViewModel = createLoginViewModel()
        val userNameWithSpaces = " test user "

        loginViewModel.onUserNameValueChange(userNameWithSpaces)
        loginViewModel.onPasswordValueChange("password")

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userNameWithSpaces.trim())
    }

    @Test
    fun `onPasswordValueChange should treat whitespace as valid input when called`() {
        loginViewModel = createLoginViewModel()
        val passwordWithSpaces = " test password "

        loginViewModel.onPasswordValueChange(passwordWithSpaces)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(passwordWithSpaces)
    }

    @Test
    fun `onLoginClick should emit LoginSuccessfully when login is successful`() = runTest {
        loginViewModel = createLoginViewModel()
        coEvery { loginUseCase.invoke(any(), any()) } returns Unit

        loginViewModel.snackBarState.test {
            loginViewModel.onLoginClick()
            skipItems(1)
            val snackBar = awaitItem()
            assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.LoginSuccessfully)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClick should emit EmptyFieldError when EmptyFieldException is thrown`() = runTest {
        loginViewModel = createLoginViewModel()
        coEvery { loginUseCase.invoke(any(), any()) } throws EmptyFieldException()

        loginViewModel.snackBarState.test {
            loginViewModel.onLoginClick()
            skipItems(1)
            val snackBar = awaitItem()
            assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.EmptyFieldError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClick should emit InValidPasswordError when InValidPasswordException is thrown`() =
        runTest {
            loginViewModel = createLoginViewModel()
            coEvery { loginUseCase.invoke(any(), any()) } throws InValidPasswordException()

            loginViewModel.snackBarState.test {
                loginViewModel.onLoginClick()
                skipItems(1)
                val snackBar = awaitItem()
                assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.InValidPasswordError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onLoginClick should emit InValidCredentialsError when InValidUserCredentialException is thrown`() =
        runTest {
            loginViewModel = createLoginViewModel()
            coEvery { loginUseCase.invoke(any(), any()) } throws InValidUserCredentialException()

            loginViewModel.snackBarState.test {
                loginViewModel.onLoginClick()
                skipItems(1)
                val snackBar = awaitItem()
                assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.InValidCredentialsError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onLoginClick should emit NoInternetException when NoInternetException is thrown`() =
        runTest {
            loginViewModel = createLoginViewModel()
            coEvery { loginUseCase.invoke(any(), any()) } throws NoInternetException()

            loginViewModel.snackBarState.test {
                loginViewModel.onLoginClick()
                skipItems(1)
                val snackBar = awaitItem()
                assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.NoInternetException)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onLoginClick should emit NetworkError when UnKnownNetworkException is thrown`() = runTest {
        loginViewModel = createLoginViewModel()
        coEvery { loginUseCase.invoke(any(), any()) } throws UnKnownNetworkException()

        loginViewModel.snackBarState.test {
            loginViewModel.onLoginClick()
            skipItems(1)
            val snackBar = awaitItem()
            assertThat(snackBar.message).isEqualTo(BaseSnackBarMessage.NetworkError)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
