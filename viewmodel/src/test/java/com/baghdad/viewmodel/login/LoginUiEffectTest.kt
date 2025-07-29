package com.baghdad.viewmodel.login

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LoginUiEffectTest {

    @Test
    fun `NavigateBack should be singleton instance`() {
        // Given
        val effect1 = LoginUiEffect.NavigateBack

        // When
        val effect2 = LoginUiEffect.NavigateBack

        // Then
        assertSame(effect1, effect2)
    }

    @Test
    fun `NavigateToHome should be singleton instance`() {
        // Given
        val effect1 = LoginUiEffect.NavigateToHome

        // When
        val effect2 = LoginUiEffect.NavigateToHome

        // Then
        assertSame(effect1, effect2)
    }

    @Test
    fun `NavigateToRegister should be singleton instance`() {
        // Given
        val effect1 = LoginUiEffect.NavigateToRegister

        // When
        val effect2 = LoginUiEffect.NavigateToRegister

        // Then
        assertSame(effect1, effect2)
    }

    @Test
    fun `NavigateToForgotPassword should be singleton instance`() {
        // Given
        val effect1 = LoginUiEffect.NavigateToForgotPassword

        // When
        val effect2 = LoginUiEffect.NavigateToForgotPassword

        // Then
        assertSame(effect1, effect2)
    }

    @Test
    fun `sealed class should support when expression exhaustively`() {
        // Given
        val effect: LoginUiEffect = LoginUiEffect.NavigateToHome

        // When
        val result = when (effect) {
            LoginUiEffect.NavigateBack -> "back"
            LoginUiEffect.NavigateToHome -> "home"
            LoginUiEffect.NavigateToRegister -> "register"
            LoginUiEffect.NavigateToForgotPassword -> "forgot"
        }

        // Then
        assertEquals("home", result)
    }

    @Test
    fun `each effect should be equal to itself`() {
        // Given
        val back = LoginUiEffect.NavigateBack
        val home = LoginUiEffect.NavigateToHome
        val register = LoginUiEffect.NavigateToRegister
        val forgot = LoginUiEffect.NavigateToForgotPassword

        // When & Then
        assertEquals(back, back)
        assertEquals(home, home)
        assertEquals(register, register)
        assertEquals(forgot, forgot)
    }

    @Test
    fun `each effect should have consistent hashCode`() {
        // Given
        val back = LoginUiEffect.NavigateBack
        val home = LoginUiEffect.NavigateToHome
        val register = LoginUiEffect.NavigateToRegister
        val forgot = LoginUiEffect.NavigateToForgotPassword

        // When & Then
        assertEquals(back.hashCode(), back.hashCode())
        assertEquals(home.hashCode(), home.hashCode())
        assertEquals(register.hashCode(), register.hashCode())
        assertEquals(forgot.hashCode(), forgot.hashCode())
    }

    @Test
    fun `LoginUiEffect sealed class should include all known effects`() {
        // Given
        val effects = listOf(
            LoginUiEffect.NavigateBack,
            LoginUiEffect.NavigateToHome,
            LoginUiEffect.NavigateToRegister,
            LoginUiEffect.NavigateToForgotPassword
        )

        // When
        val size = effects.size

        // Then
        assertEquals(4, size)
    }

    @Test
    fun `different effects should not be equal`() {
        // Given
        val back = LoginUiEffect.NavigateBack
        val home = LoginUiEffect.NavigateToHome
        val register = LoginUiEffect.NavigateToRegister
        val forgot = LoginUiEffect.NavigateToForgotPassword

        // When & Then
        assertNotEquals(back, home)
        assertNotEquals(home, register)
        assertNotEquals(register, forgot)
        assertNotEquals(forgot, back)
    }
}
