package com.baghdad.viewmodel.login

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
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should have correct default values when initialized`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertThat(state.isLoading).isFalse()
        assertThat(state.userName).isEmpty()
        assertThat(state.password).isEmpty()
        assertThat(state.isPasswordVisible).isFalse()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onUserNameValueChange() should update userName and validate fields when called with non-empty value`() {
        // Given
        val userName = "testuser"

        // When
        viewModel.onUserNameValueChange(userName)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onUserNameValueChange() should set isAnyFieldEmpty to true when called with empty string`() {
        // Given
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        // When
        viewModel.onUserNameValueChange("")

        // Then
        val state = viewModel.uiState.value
        assertThat(state.userName).isEmpty()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onPasswordValueChange() should update password and validate fields when called with non-empty value`() {
        // Given
        val password = "testpass"

        // When
        viewModel.onPasswordValueChange(password)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(password)
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `onPasswordValueChange() should set isAnyFieldEmpty to true when called with empty string`() {
        // Given
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        // When
        viewModel.onPasswordValueChange("")

        // Then
        val state = viewModel.uiState.value
        assertThat(state.password).isEmpty()
        assertThat(state.isAnyFieldEmpty).isTrue()
    }

    @Test
    fun `isAnyFieldEmpty should be false when both userName and password fields are filled`() {
        // Given
        val userName = "testuser"
        val password = "testpass"

        // When
        viewModel.onUserNameValueChange(userName)
        viewModel.onPasswordValueChange(password)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userName)
        assertThat(state.password).isEqualTo(password)
        assertThat(state.isAnyFieldEmpty).isFalse()
    }

    @Test
    fun `togglePasswordVisibility() should toggle isPasswordVisible from false to true when called`() {
        // Given
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()

        // When
        viewModel.togglePasswordVisibility()

        // Then
        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()
    }

    @Test
    fun `togglePasswordVisibility() should toggle isPasswordVisible from true to false when called twice`() {
        // Given
        viewModel.togglePasswordVisibility()
        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()

        // When
        viewModel.togglePasswordVisibility()

        // Then
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
    }

    @Test
    fun `onRegisterClicked() should emit NavigateToRegister effect when called`() = runTest {
        // Given
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        // When
        viewModel.onRegisterClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(effect).isEqualTo(LoginUiEffect.NavigateToRegister)
        job.cancel()
    }

    @Test
    fun `onForgotPasswordClicked() should emit NavigateToForgotPassword effect when called`() =
        runTest {
            // Given
            var effect: LoginUiEffect? = null
            val job = launch {
                viewModel.uiEffect.collect { effect = it }
            }

            // When
            viewModel.onForgotPasswordClicked()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertThat(effect).isEqualTo(LoginUiEffect.NavigateToForgotPassword)
            job.cancel()
        }

    @Test
    fun `onLoginClicked() should handle UnAuthorizedException correctly when login fails`() =
        runTest {
            // Given
            val userName = "testuser"
            val password = "wrongpass"
            viewModel.onUserNameValueChange(userName)
            viewModel.onPasswordValueChange(password)

            coEvery { loginUseCase.invoke(userName, password) } throws UnAuthorizedException()

            // When
            viewModel.onLoginClicked()
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.isLoading).isFalse()
        }

    @Test
    fun `onLoginSuccess() should emit NavigateToHome effect when called`() = runTest {
        // Given
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        // When
        viewModel.onLoginSuccess()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(effect).isEqualTo(LoginUiEffect.NavigateToHome)
        job.cancel()
    }

    @Test
    fun `onLoginError() should handle UnAuthorizedException correctly when called`() {
        // Given
        val exception = UnAuthorizedException()

        // When
        viewModel.onLoginError(exception)

        // Then
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError() should handle NoInternetException correctly when called`() {
        // Given
        val exception = NoInternetException()

        // When
        viewModel.onLoginError(exception)

        // Then
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLoginError() should handle unknown exceptions correctly when called`() {
        // Given
        val exception = RuntimeException("Unknown error")

        // When
        viewModel.onLoginError(exception)

        // Then
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `uiState should maintain correct values when multiple field changes occur`() {
        // When
        viewModel.onUserNameValueChange("user1")
        viewModel.onPasswordValueChange("pass1")
        viewModel.togglePasswordVisibility()
        viewModel.onUserNameValueChange("user2")
        viewModel.onPasswordValueChange("pass2")

        // Then
        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo("user2")
        assertThat(state.password).isEqualTo("pass2")
        assertThat(state.isPasswordVisible).isTrue()
        assertThat(state.isAnyFieldEmpty).isFalse()
    }

    @Test
    fun `onUserNameValueChange() should treat whitespace as valid input when called`() {
        // Given
        val userNameWithSpaces = " test user "

        // When
        viewModel.onUserNameValueChange(userNameWithSpaces)
        viewModel.onPasswordValueChange("password")

        // Then
        val state = viewModel.uiState.value
        assertThat(state.userName).isEqualTo(userNameWithSpaces)
        assertThat(state.isAnyFieldEmpty).isFalse()
    }

    @Test
    fun `onPasswordValueChange() should treat whitespace as valid input when called`() {
        // Given
        val passwordWithSpaces = " test password "

        // When
        viewModel.onUserNameValueChange("username")
        viewModel.onPasswordValueChange(passwordWithSpaces)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(passwordWithSpaces)
        assertThat(state.isAnyFieldEmpty).isFalse()
    }
}