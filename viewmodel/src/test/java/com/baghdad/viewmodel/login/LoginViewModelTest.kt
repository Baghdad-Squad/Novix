package com.baghdad.viewmodel.login

import app.cash.turbine.test
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnAuthorizedException
import com.baghdad.domain.usecase.login.LoginUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginViewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        loginViewModel = LoginViewModel(loginUseCase, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onBackClick should navigate back when clicked`() = runTest {
        loginViewModel.onBackClick()

        loginViewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onUserNameValueChange should update userName and validate fields when called with non-empty value`() {
        val userName = "testuser"

        loginViewModel.onUserNameValueChange(userName)

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
    }

    @Test
    fun `onUserNameValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        loginViewModel.onUserNameValueChange("user")

        loginViewModel.onUserNameValueChange("")

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEmpty()
    }

    @Test
    fun `onPasswordValueChange should update password and validate fields when called with non-empty value`() {
        val password = "testpass"

        loginViewModel.onPasswordValueChange(password)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
    }

    @Test
    fun `onPasswordValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        loginViewModel.onPasswordValueChange("pass")

        loginViewModel.onPasswordValueChange("")

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEmpty()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }



    @Test
    fun `isAnyFieldEmpty should be false when userName field is filled`() {
        val userName = "testuser"

        loginViewModel.onUserNameValueChange(userName)

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
    }

    @Test
    fun `isAnyFieldEmpty should be false when password fields is filled`() {
        val password = "testpass"

        loginViewModel.onPasswordValueChange(password)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
    }

    @Test
    fun `onTogglePasswordChange should toggle isPasswordVisible from false to true when called`() {
        assertThat(loginViewModel.uiState.value.isPasswordVisible).isFalse()

        loginViewModel.onTogglePasswordChange()

        assertThat(loginViewModel.uiState.value.isPasswordVisible).isTrue()
    }

    @Test
    fun `togglePasswordVisibility should toggle isPasswordVisible from true to false when called twice`() =
        runTest {
            loginViewModel.onTogglePasswordChange()
            assertThat(loginViewModel.uiState.value.isPasswordVisible).isTrue()

            loginViewModel.onTogglePasswordChange()

            assertThat(loginViewModel.uiState.value.isPasswordVisible).isFalse()
        }

    @Test
    fun `onRegisterClick should emit NavigateToRegister effect when called`() = runTest {

        loginViewModel.onRegisterClick()

        loginViewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateToRegister)
        }

    }

    @Test
    fun `onForgotPasswordClick should emit NavigateToForgotPassword effect when called`() =
        runTest {
            loginViewModel.onForgotPasswordClick()

            loginViewModel.uiEffect.test {
                val effect = awaitItem()
                assertThat(effect).isEqualTo(LoginUiEffect.NavigateToForgotPassword)
            }
        }

    @Test
    fun `onLoginClick should handle UnAuthorizedException correctly when login fails`() =
        runTest {
            val userName = "testuser"
            val password = "wrongpass"
            loginViewModel.onUserNameValueChange(userName)
            loginViewModel.onPasswordValueChange(password)

            coEvery { loginUseCase.invoke(userName, password) } throws UnAuthorizedException()

            loginViewModel.onLoginClick()
            advanceUntilIdle()

            assertThat(loginViewModel.uiState.value.isLoading).isFalse()
        }


    @Test
    fun `onLoginError should handle UnAuthorizedException correctly when called`() {
        val exception = UnAuthorizedException()

        loginViewModel.onLoginError(exception)

        assertThat(loginViewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError should handle NoInternetException correctly when called`() {
        val exception = NoInternetException()

        loginViewModel.onLoginError(exception)

        assertThat(loginViewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError should handle unknown exceptions correctly when called`() {
        val exception = RuntimeException("Unknown error")

        loginViewModel.onLoginError(exception)

        assertThat(loginViewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onUserNameValueChange should treat whitespace as valid input when called`() {
        val userNameWithSpaces = " test user "

        loginViewModel.onUserNameValueChange(userNameWithSpaces)
        loginViewModel.onPasswordValueChange("password")

        val state = loginViewModel.uiState.value
        assertThat(state.userName).isEqualTo(userNameWithSpaces.trim())
    }

    @Test
    fun `onPasswordValueChange should treat whitespace as valid input when called`() {
        val passwordWithSpaces = " test password "

        loginViewModel.onPasswordValueChange(passwordWithSpaces)

        val state = loginViewModel.uiState.value
        assertThat(state.password).isEqualTo(passwordWithSpaces)
    }
}
