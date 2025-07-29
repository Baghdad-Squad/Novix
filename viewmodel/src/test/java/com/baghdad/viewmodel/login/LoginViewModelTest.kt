package com.baghdad.viewmodel.login

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnAuthorizedException
import com.baghdad.domain.usecase.login.LoginUseCase
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun `initial state should have correct default values`() {
        // Given & When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.isLoading)
        assertEquals("", state.userName)
        assertEquals("", state.password)
        assertFalse(state.isPasswordVisible)
        assertTrue(state.isAnyFieldEmpty)
    }

    @Test
    fun `onUserNameValueChange should update userName and validate fields`() {
        // Given
        val userName = "testuser"

        // When
        viewModel.onUserNameValueChange(userName)

        // Then
        val state = viewModel.uiState.value
        assertEquals(userName, state.userName)
        assertTrue(state.isAnyFieldEmpty)
    }

    @Test
    fun `onUserNameValueChange with empty string should set isAnyFieldEmpty to true`() {
        // Given
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        // When
        viewModel.onUserNameValueChange("")

        // Then
        val state = viewModel.uiState.value
        assertEquals("", state.userName)
        assertTrue(state.isAnyFieldEmpty)
    }

    @Test
    fun `onPasswordValueChange should update password and validate fields`() {
        // Given
        val password = "testpass"

        // When
        viewModel.onPasswordValueChange(password)

        // Then
        val state = viewModel.uiState.value
        assertEquals(password, state.password)
        assertTrue(state.isAnyFieldEmpty)
    }

    @Test
    fun `onPasswordValueChange with empty string should set isAnyFieldEmpty to true`() {
        // Given
        viewModel.onUserNameValueChange("user")
        viewModel.onPasswordValueChange("pass")

        // When
        viewModel.onPasswordValueChange("")

        // Then
        val state = viewModel.uiState.value
        assertEquals("", state.password)
        assertTrue(state.isAnyFieldEmpty)
    }

    @Test
    fun `when both userName and password are filled isAnyFieldEmpty should be false`() {
        // Given
        val userName = "testuser"
        val password = "testpass"

        // When
        viewModel.onUserNameValueChange(userName)
        viewModel.onPasswordValueChange(password)

        // Then
        val state = viewModel.uiState.value
        assertEquals(userName, state.userName)
        assertEquals(password, state.password)
        assertFalse(state.isAnyFieldEmpty)
    }

    @Test
    fun `togglePasswordVisibility should toggle isPasswordVisible from false to true`() {
        // Given
        assertFalse(viewModel.uiState.value.isPasswordVisible)

        // When
        viewModel.togglePasswordVisibility()

        // Then
        assertTrue(viewModel.uiState.value.isPasswordVisible)
    }

    @Test
    fun `togglePasswordVisibility should toggle isPasswordVisible from true to false`() {
        // Given
        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.uiState.value.isPasswordVisible)

        // When
        viewModel.togglePasswordVisibility()

        // Then
        assertFalse(viewModel.uiState.value.isPasswordVisible)
    }

    @Test
    fun `onRegisterClicked should emit NavigateToRegister effect`() = runTest {
        // Given
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        // When
        viewModel.onRegisterClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(LoginUiEffect.NavigateToRegister, effect)
        job.cancel()
    }

    @Test
    fun `onForgotPasswordClicked should emit NavigateToForgotPassword effect`() = runTest {
        // Given
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        // When
        viewModel.onForgotPasswordClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(LoginUiEffect.NavigateToForgotPassword, effect)
        job.cancel()
    }

    @Test
    fun `onLoginClicked with UnAuthorizedException should handle error correctly`() = runTest {
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
        assertFalse(viewModel.uiState.value.isLoading)
    }


    @Test
    fun `onLoginSuccess should emit NavigateToHome effect`() = runTest {
        // Given
        var effect: LoginUiEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect = it }
        }

        // When
        viewModel.onLoginSuccess()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(LoginUiEffect.NavigateToHome, effect)
        job.cancel()
    }

    @Test
    fun `onLoginError with UnAuthorizedException should handle correctly`() {
        // Given
        val exception = UnAuthorizedException()

        // When
        viewModel.onLoginError(exception)

    }

    @Test
    fun `onLoginError with NoInternetException should handle correctly`() {
        // Given
        val exception = NoInternetException()

        // When
        viewModel.onLoginError(exception)

    }

    @Test
    fun `onLoginError with unknown exception should handle correctly`() {
        // Given
        val exception = RuntimeException("Unknown error")

        // When
        viewModel.onLoginError(exception)

    }


    @Test
    fun `multiple field changes should maintain correct state`() {
        // Given & When
        viewModel.onUserNameValueChange("user1")
        viewModel.onPasswordValueChange("pass1")
        viewModel.togglePasswordVisibility()
        viewModel.onUserNameValueChange("user2")
        viewModel.onPasswordValueChange("pass2")

        // Then
        val state = viewModel.uiState.value
        assertEquals("user2", state.userName)
        assertEquals("pass2", state.password)
        assertTrue(state.isPasswordVisible)
        assertFalse(state.isAnyFieldEmpty)
    }

    @Test
    fun `onUserNameValueChange with whitespace should be treated as valid input`() {
        // Given
        val userNameWithSpaces = " test user "

        // When
        viewModel.onUserNameValueChange(userNameWithSpaces)
        viewModel.onPasswordValueChange("password")

        // Then
        val state = viewModel.uiState.value
        assertEquals(userNameWithSpaces, state.userName)
        assertFalse(state.isAnyFieldEmpty)
    }

    @Test
    fun `onPasswordValueChange with whitespace should be treated as valid input`() {
        // Given
        val passwordWithSpaces = " test password "

        // When
        viewModel.onUserNameValueChange("username")
        viewModel.onPasswordValueChange(passwordWithSpaces)

        // Then
        val state = viewModel.uiState.value
        assertEquals(passwordWithSpaces, state.password)
        assertFalse(state.isAnyFieldEmpty)
    }
}