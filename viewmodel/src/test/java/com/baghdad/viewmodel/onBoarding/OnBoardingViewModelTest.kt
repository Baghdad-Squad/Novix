package com.baghdad.viewmodel.onBoarding

import app.cash.turbine.test
import com.baghdad.domain.usecase.appConfigurations.SetFirstTimeLaunchAppUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingViewModelTest {
    private val setFirstTimeLaunchAppUseCase: SetFirstTimeLaunchAppUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val viewModel: OnBoardingViewModel =
        OnBoardingViewModel(setFirstTimeLaunchAppUseCase, testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { setFirstTimeLaunchAppUseCase(any()) } returns Unit
    }
    
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.cancel()
        TestScope().cancel()
    }

    @Test
    fun `should initial state be page 0 when viewModel initialized`() = runTest {
        val initialState = viewModel.uiState.value

        assertThat(initialState.currentPage).isEqualTo(0)
    }

    @Test
    fun `onNextButtonClick should increment current page when not on last page`() = runTest {
        val initialState = viewModel.uiState.value
        assertThat(initialState.currentPage).isEqualTo(0)

        viewModel.onNextButtonClick(3)
        advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertThat(updatedState.currentPage).isEqualTo(1)
    }

    @Test
    fun `onBackButtonClick should back to page 0 when the page is second`() = runTest {
        val initialPage = viewModel.uiState.value.currentPage

        viewModel.onNextButtonClick(3)
        advanceUntilIdle()
        viewModel.onBackButtonClick()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.currentPage).isEqualTo(initialPage)
    }

    @Test
    fun `onBackButtonClick should stay on page 0 when on first page`() = runTest {
        val initialPage = viewModel.uiState.value.currentPage

        viewModel.onBackButtonClick()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.currentPage).isEqualTo(initialPage)
    }

    @Test
    fun `onNextButtonClick should navigate to welcome screen when on last page`() = runTest {
        val pageSize = 3
        viewModel.uiEffect.test {
            for (i in 1..pageSize) {
                viewModel.onNextButtonClick(pageSize)
                advanceUntilIdle()
            }
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(OnBoardingEffect.NavigateToWelcomeToNovix::class.java)
        }
    }

    @Test
    fun `onSkipButtonClick should navigate to welcome screen`() = runTest {
        var receivedEffect: OnBoardingEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        viewModel.onSkipButtonClick()
        advanceUntilIdle()

        assertThat(receivedEffect is OnBoardingEffect.NavigateToWelcomeToNovix).isTrue()
        job.cancel()
    }

}