package com.baghdad.viewmodel.onBoarding

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
    private lateinit var setFirstTimeLaunchAppUseCase: SetFirstTimeLaunchAppUseCase
    private lateinit var viewModel: OnBoardingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        setFirstTimeLaunchAppUseCase = mockk(relaxed = true)
        coEvery { setFirstTimeLaunchAppUseCase(any()) } returns Unit
        viewModel = OnBoardingViewModel(setFirstTimeLaunchAppUseCase, testDispatcher)
    }


    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.cancel()
        TestScope().cancel()
    }

    @Test
    fun `should initial state be page 0 when viewModel initialized`() = runTest {
        // When
        val initialState = viewModel.uiState.value

        // Then
        assertThat(initialState.currentPage).isEqualTo(0)
    }

    @Test
    fun `onNextButtonClick should increment current page when not on last page`() = runTest {
        // Given
        val initialState = viewModel.uiState.value
        assertThat(initialState.currentPage).isEqualTo(0)

        // When
        viewModel.onNextButtonClick(3)
        advanceUntilIdle()

        // Then
        val updatedState = viewModel.uiState.value
        assertThat(updatedState.currentPage).isEqualTo(1)
    }

    @Test
    fun `onBackButtonClick should back to page 0 when the page is second`() = runTest {
        // Given
        val initialPage = viewModel.uiState.value.currentPage

        // When
        viewModel.onNextButtonClick(3)
        advanceUntilIdle()
        viewModel.onBackButtonClick()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(initialPage)
    }

    @Test
    fun `onBackButtonClick should stay on page 0 when on first page`() = runTest {
        // Given
        val initialPage = viewModel.uiState.value.currentPage

        // When
        viewModel.onBackButtonClick()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(initialPage)
    }

    @Test
    fun `onNextButtonClick should navigate to welcome screen when on last page`() = runTest {
        // Given
        val pageSize = 3
        var receivedEffect: OnBoardingEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        for (i in 1..pageSize) {
            viewModel.onNextButtonClick(pageSize)
            advanceUntilIdle()
        }

        // Then
        assertThat(receivedEffect is OnBoardingEffect.NavigateToWelcomeToNovix).isTrue()
        job.cancel()
    }

    @Test
    fun `onSkipButtonClick should navigate to welcome screen`() = runTest {
        // Given
        var receivedEffect: OnBoardingEffect? = null
        val job = launch {
            viewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        viewModel.onSkipButtonClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is OnBoardingEffect.NavigateToWelcomeToNovix).isTrue()
        job.cancel()
    }

}