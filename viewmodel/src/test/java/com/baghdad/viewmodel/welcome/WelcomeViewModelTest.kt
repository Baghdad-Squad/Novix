package com.baghdad.viewmodel.welcome

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WelcomeViewModelTest {
    private lateinit var viewModel: WelcomeViewModel
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        viewModel = WelcomeViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onClickLogin sends NavigateToLogin effect`() = runTest {
        // Given
        var receivedEffect: WelcomeEffect? = null
        val job = launch {
            viewModel.uiEffect.collect {
                receivedEffect = it
            }
        }
        // When
        viewModel.onClickLogin()
        advanceUntilIdle()
        // Then
        assertThat(WelcomeEffect.NavigateToLogin == receivedEffect).isTrue()
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onClickContinueAsGuest sends NavigateToContinueAsGuest effect`() = runTest {
        // Given
        var receivedEffect: WelcomeEffect? = null
        val job = launch {
            viewModel.uiEffect.collect {
                receivedEffect = it
            }
        }
        // When
        viewModel.onClickContinueAsGuest()
        advanceUntilIdle()
        // Then
        assertThat(WelcomeEffect.NavigateToContinueAsGuest == receivedEffect).isTrue()
        job.cancel()
    }
}