package com.baghdad.viewmodel.main

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MainStateTest {

    @Test
    fun `MainState should create state with default values when constructor called`() {
        // When
        val state = MainState()

        // Then
        assertThat(state.isLoggedIn).isNull()
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `MainState should set values correctly when assign new parameter`() {
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
    fun `MainState should create new state with updated isLoggedIn when copy is called`() {
        // Given
        val originalState = MainState(isLoggedIn = null)
        val newLoggedInStatus = true

        // When
        val newState = originalState.copy(isLoggedIn = newLoggedInStatus)

        // Then
        assertThat(newState.isLoggedIn).isTrue()
        assertThat(originalState.isLoggedIn).isNull()
    }

    @Test
    fun `MainState should create new state with updated isLoading when copy is called`() {
        // Given
        val originalState = MainState(isLoading = true)
        val newLoadingStatus = false

        // When
        val newState = originalState.copy(isLoading = newLoadingStatus)

        // Then
        assertThat(newState.isLoading).isFalse()
        assertThat(originalState.isLoading).isTrue()
    }

    @Test
    fun `MainState should be equal when all properties are identical`() {
        // Given
        val state1 = MainState(isLoggedIn = true, isLoading = false)
        val state2 = MainState(isLoggedIn = true, isLoading = false)

        // Then
        assertThat(state1).isEqualTo(state2)
    }

    @Test
    fun `MainState should not be equal when properties differ`() {
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
    fun `MainState should have same hashCode when properties are identical`() {
        // Given
        val state1 = MainState(isLoggedIn = true, isLoading = false)
        val state2 = MainState(isLoggedIn = true, isLoading = false)

        // Then
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
    }

    @Test
    fun `MainState should have different hashCode when properties differ`() {
        // Given
        val state1 = MainState(isLoggedIn = true)
        val state2 = MainState(isLoggedIn = false)
        val state3 = MainState(isLoading = false)

        // Then
        assertThat(state1.hashCode()).isNotEqualTo(state2.hashCode())
        assertThat(state1.hashCode()).isNotEqualTo(state3.hashCode())
    }

    @Test
    fun `MainState should include all properties in toString`() {
        // Given
        val state = MainState(isLoggedIn = true, isLoading = false)

        // When
        val stringRepresentation = state.toString()

        // Then
        assertThat(stringRepresentation).contains("isLoggedIn=true")
        assertThat(stringRepresentation).contains("isLoading=false")
    }

    @Test
    fun `MainState should be equal when isLoggedIn is null in both states`() {
        // Given
        val state1 = MainState(isLoggedIn = null)
        val state2 = MainState(isLoggedIn = null)
        val state3 = MainState(isLoggedIn = true)

        // Then
        assertThat(state1).isEqualTo(state2)
        assertThat(state1).isNotEqualTo(state3)
    }

    @Test
    fun `MainState should have same hashCode when isLoggedIn is null in both states`() {
        // Given
        val state1 = MainState(isLoggedIn = null)
        val state2 = MainState(isLoggedIn = null)
        val state3 = MainState(isLoggedIn = true)

        // Then
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
        assertThat(state1.hashCode()).isNotEqualTo(state3.hashCode())
    }
}