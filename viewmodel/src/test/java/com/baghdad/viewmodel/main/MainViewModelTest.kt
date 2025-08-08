package com.baghdad.viewmodel.main

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.onBoarding.IsFirstTimeLaunchAppUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    // Given
    private val mockAuthRepository: AuthenticationRepository = mockk()
    private val isLoggedInUseCase = IsLoggedInUseCase(mockAuthRepository)
    private val isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase = mockk()
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isUserLoggedIn should be called when ViewModel starts`() = runTest {
        // Given
        coEvery { mockAuthRepository.isUserLoggedIn() } returns true

        // When
        viewModel = MainViewModel(isLoggedInUseCase, isFirstTimeLaunchAppUseCase )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { mockAuthRepository.isUserLoggedIn() }
    }

    @Test
    fun `isLoading should remain true when authentication check is not yet complete`() = runTest {
        // Given
        coEvery { mockAuthRepository.isUserLoggedIn() } coAnswers {
            delay(1000)
            true
        }

        // When
        viewModel = MainViewModel(isLoggedInUseCase, isFirstTimeLaunchAppUseCase)
        testDispatcher.scheduler.advanceTimeBy(500)

        // Then
        assertThat(viewModel.uiState.value.isLoading).isTrue()
    }
}