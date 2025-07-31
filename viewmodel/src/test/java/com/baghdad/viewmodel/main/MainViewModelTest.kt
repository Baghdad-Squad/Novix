package com.baghdad.viewmodel.main

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
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
    fun `should initialize with loading state and null login status`() = runTest {
        // When
        viewModel = MainViewModel(isLoggedInUseCase)

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(
            MainState(
                isLoggedIn = null,
                isLoading = true
            )
        )
    }

    @Test
    fun `should check login status on initialization`() = runTest {
        // Given
        coEvery { mockAuthRepository.isUserLoggedIn() } returns true

        // When
        viewModel = MainViewModel(isLoggedInUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { mockAuthRepository.isUserLoggedIn() }
    }

    @Test
    fun `should show loading while authentication check is in progress`() = runTest {
        // Given
        coEvery { mockAuthRepository.isUserLoggedIn() } coAnswers {
            delay(1000) // Simulate long-running operation
            true
        }

        // When
        viewModel = MainViewModel(isLoggedInUseCase)
        testDispatcher.scheduler.advanceTimeBy(500) // Advance partially

        // Then
        assertThat(viewModel.uiState.value.isLoading).isTrue()
    }


}