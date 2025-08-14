package com.baghdad.viewmodel.onBoarding

import app.cash.turbine.test
import com.baghdad.domain.usecase.appConfigurations.SetFirstTimeLaunchAppUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
        val expectedPage = 0

        assertThat(initialState.currentPage).isEqualTo(expectedPage)
    }

    @Test
    fun `onNextButtonClick should increment current page when not on last page`() = runTest {
        viewModel.onNextButtonClick(PAGE_SIZE)
        advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertThat(updatedState.currentPage).isEqualTo(1)
    }

    @Test
    fun `onBackButtonClick should back to page 0 when the page is second`() = runTest {
        val initialPage = viewModel.uiState.value.currentPage

        viewModel.onNextButtonClick(PAGE_SIZE)
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
        for (i in 1..PAGE_SIZE) {
            viewModel.onNextButtonClick(PAGE_SIZE)
        }

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(OnBoardingEffect.NavigateToWelcomeToNovix)
        }
    }

    @Test
    fun `onSkipButtonClick should navigate to welcome screen`() = runTest {
        viewModel.onSkipButtonClick()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(OnBoardingEffect.NavigateToWelcomeToNovix)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        const val PAGE_SIZE = 3
    }

}