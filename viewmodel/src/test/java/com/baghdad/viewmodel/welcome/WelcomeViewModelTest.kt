package com.baghdad.viewmodel.welcome

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
        viewModel.uiEffect.test {

            viewModel.onClickLogin()

            assertThat(awaitItem()).isEqualTo(WelcomeEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onClickContinueAsGuest sends NavigateToContinueAsGuest effect`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onClickContinueAsGuest()

            assertThat(awaitItem()).isEqualTo(WelcomeEffect.NavigateToContinueAsGuest)
            cancelAndIgnoreRemainingEvents()
        }
    }
}