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
import kotlinx.coroutines.launch
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
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onBackClick should navigate back when clicked`() = runTest {
        viewModel.onBackClick()

        viewModel.uiEffect.test{
            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onUserNameValueChange should update userName and validate fields when called with non-empty value`() {
        val userName = "testuser"

        viewModel.onUserNameValueChange(userName)

        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onUserNameValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        viewModel.onUserNameValueChange("")

        val state = viewModel.uiState.value
        assertThat(state.userName).isEmpty()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onPasswordValueChange should update password and validate fields when called with non-empty value`() {
        val password = "testpass"

        viewModel.onPasswordValueChange(password)

        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onPasswordValueChange should set isAnyFieldEmpty to true when called with empty string`() {
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        viewModel.onPasswordValueChange("")

        val state = viewModel.uiState.value
        assertThat(state.password).isEmpty()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `isAnyFieldEmpty should be false when userName field is filled`() {
        val userName = "testuser"

        viewModel.onUserNameValueChange(userName)

        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
    }

    @Test
    fun `isAnyFieldEmpty should be false when password fields is filled`() {
        val password = "testpass"

        viewModel.onPasswordValueChange(password)

        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
    }

    @Test
    fun `onTogglePasswordChange should toggle isPasswordVisible from false to true when called`() {
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()

        viewModel.onTogglePasswordChange()

        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()
    }

    @Test
    fun `togglePasswordVisibility should toggle isPasswordVisible from true to false when called twice`() =
        runTest {
            viewModel.onTogglePasswordChange()
            assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()

            viewModel.onTogglePasswordChange()

            assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
        }

    @Test
    fun `onRegisterClick should emit NavigateToRegister effect when called`() = runTest {
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        viewModel.onRegisterClick()
        advanceUntilIdle()

        assertThat(effect).isEqualTo(LoginUiEffect.NavigateToRegister)
        job.cancel()
    }

    @Test
    fun `onForgotPasswordClick should emit NavigateToForgotPassword effect when called`() =
        runTest {
            var effect: LoginUiEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect = it }
            }

            viewModel.onForgotPasswordClick()
            advanceUntilIdle()

            assertThat(effect).isEqualTo(LoginUiEffect.NavigateToForgotPassword)
            job.cancel()
        }

    @Test
    fun `onLoginClick should handle UnAuthorizedException correctly when login fails`() =
        runTest {
            val userName = "testuser"
            val password = "wrongpass"
            viewModel.onUserNameValueChange(userName)
            viewModel.onPasswordValueChange(password)

            coEvery { loginUseCase.invoke(userName, password) } throws UnAuthorizedException()

            viewModel.onLoginClick()
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }


    @Test
    fun `onLoginError should handle UnAuthorizedException correctly when called`() {
        val exception = UnAuthorizedException()

        viewModel.onLoginError(exception)

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError should handle NoInternetException correctly when called`() {
        val exception = NoInternetException()

        viewModel.onLoginError(exception)

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError should handle unknown exceptions correctly when called`() {
        val exception = RuntimeException("Unknown error")

        viewModel.onLoginError(exception)

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onUserNameValueChange should treat whitespace as valid input when called`() {
        val userNameWithSpaces = " test user "

        viewModel.onUserNameValueChange(userNameWithSpaces)
        viewModel.onPasswordValueChange("password")

        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userNameWithSpaces.trim())
        assertThat(state.isAnyFieldEmpty).isFalse()
    }

    @Test
    fun `onPasswordValueChange should treat whitespace as valid input when called`() {
        val passwordWithSpaces = " test password "

        viewModel.onPasswordValueChange(passwordWithSpaces)

        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(passwordWithSpaces)
    }
}
