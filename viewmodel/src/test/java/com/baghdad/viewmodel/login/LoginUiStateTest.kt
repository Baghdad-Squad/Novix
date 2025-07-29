package com.baghdad.viewmodel.login

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LoginUiStateTest {

    @Test
    fun `default state should have expected default values`() {
        // Given
        val state = LoginUiState()

        // When
        val isLoading = state.isLoading
        val userName = state.userName
        val password = state.password
        val isPasswordVisible = state.isPasswordVisible
        val isAnyFieldEmpty = state.isAnyFieldEmpty

        // Then
        assertFalse(isLoading)
        assertEquals("", userName)
        assertEquals("", password)
        assertFalse(isPasswordVisible)
        assertTrue(isAnyFieldEmpty)
    }

    @Test
    fun `state should reflect updated userName and password`() {
        // Given
        val userName = "testuser"
        val password = "123456"

        // When
        val state = LoginUiState(userName = userName, password = password, isAnyFieldEmpty = false)

        // Then
        assertEquals(userName, state.userName)
        assertEquals(password, state.password)
        assertFalse(state.isAnyFieldEmpty)
    }

    @Test
    fun `copy should create new instance with updated isLoading`() {
        // Given
        val initialState = LoginUiState()

        // When
        val newState = initialState.copy(isLoading = true)

        // Then
        assertTrue(newState.isLoading)
        assertEquals(initialState.userName, newState.userName)
        assertEquals(initialState.password, newState.password)
    }

    @Test
    fun `copy should allow toggling password visibility`() {
        // Given
        val state = LoginUiState(isPasswordVisible = false)

        // When
        val updatedState = state.copy(isPasswordVisible = true)

        // Then
        assertTrue(updatedState.isPasswordVisible)
    }

    @Test
    fun `equality check should work correctly`() {
        // Given
        val state1 = LoginUiState(userName = "a", password = "b", isAnyFieldEmpty = false)
        val state2 = LoginUiState(userName = "a", password = "b", isAnyFieldEmpty = false)

        // When & Then
        assertEquals(state1, state2)
        assertEquals(state1.hashCode(), state2.hashCode())
    }
}
