package com.baghdad.viewmodel.main

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MainStateTest {

    @Test
    fun `default constructor should create state with default values`() {
        // When
        val state = MainState()

        // Then
        assertThat(state.isLoggedIn).isNull()
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `constructor with parameters should set values correctly`() {
        // Given
        val isLoggedIn = true
        val isLoading = false

        // When
        val state = MainState(
            isLoggedIn = isLoggedIn,
            isLoading = isLoading
        )

        // Then
        assertThat(state.isLoggedIn).isTrue()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `copy function should work correctly with isLoggedIn change`() {
        // Given
        val originalState = MainState(isLoggedIn = null)
        val newLoggedInStatus = true

        // When
        val newState = originalState.copy(isLoggedIn = newLoggedInStatus)

        // Then
        assertThat(newState.isLoggedIn).isTrue()
        assertThat(originalState.isLoggedIn).isNull() // Original unchanged
    }

    @Test
    fun `copy function should work correctly with isLoading change`() {
        // Given
        val originalState = MainState(isLoading = true)
        val newLoadingStatus = false

        // When
        val newState = originalState.copy(isLoading = newLoadingStatus)

        // Then
        assertThat(newState.isLoading).isFalse()
        assertThat(originalState.isLoading).isTrue() // Original unchanged
    }

    @Test
    fun `equals should return true for identical states`() {
        // Given
        val state1 = MainState(isLoggedIn = true, isLoading = false)
        val state2 = MainState(isLoggedIn = true, isLoading = false)

        // Then
        assertThat(state1).isEqualTo(state2)
    }

    @Test
    fun `equals should return false for different states`() {
        // Given
        val state1 = MainState(isLoggedIn = true)
        val state2 = MainState(isLoggedIn = false)
        val state3 = MainState(isLoading = false)

        // Then
        assertThat(state1).isNotEqualTo(state2)
        assertThat(state1).isNotEqualTo(state3)
        assertThat(state2).isNotEqualTo(state3)
    }

    @Test
    fun `hashCode should be equal for identical states`() {
        // Given
        val state1 = MainState(isLoggedIn = true, isLoading = false)
        val state2 = MainState(isLoggedIn = true, isLoading = false)

        // Then
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
    }

    @Test
    fun `hashCode should differ for different states`() {
        // Given
        val state1 = MainState(isLoggedIn = true)
        val state2 = MainState(isLoggedIn = false)
        val state3 = MainState(isLoading = false)

        // Then
        assertThat(state1.hashCode()).isNotEqualTo(state2.hashCode())
        assertThat(state1.hashCode()).isNotEqualTo(state3.hashCode())
    }

    @Test
    fun `toString should contain all properties`() {
        // Given
        val state = MainState(isLoggedIn = true, isLoading = false)

        // When
        val stringRepresentation = state.toString()

        // Then
        assertThat(stringRepresentation).contains("isLoggedIn=true")
        assertThat(stringRepresentation).contains("isLoading=false")
    }

    @Test
    fun `should handle null isLoggedIn in equals comparison`() {
        // Given
        val state1 = MainState(isLoggedIn = null)
        val state2 = MainState(isLoggedIn = null)
        val state3 = MainState(isLoggedIn = true)

        // Then
        assertThat(state1).isEqualTo(state2)
        assertThat(state1).isNotEqualTo(state3)
    }

    @Test
    fun `should handle null isLoggedIn in hashCode calculation`() {
        // Given
        val state1 = MainState(isLoggedIn = null)
        val state2 = MainState(isLoggedIn = null)
        val state3 = MainState(isLoggedIn = true)

        // Then
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
        assertThat(state1.hashCode()).isNotEqualTo(state3.hashCode())
    }
}