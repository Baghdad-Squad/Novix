package com.baghdad.viewmodel.main

import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.onBoarding.IsFirstTimeLaunchAppUseCase
import com.baghdad.domain.usecase.savedList.SyncSavedMoviesUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppThemeUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var mockAuthRepository: AuthenticationRepository
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase
    private lateinit var getAppThemeUseCase: GetAppThemeUseCase
    private lateinit var getAppLanguageUseCase: GetAppLanguageUseCase
    private lateinit var syncSavedMoviesUseCase: SyncSavedMoviesUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockAuthRepository = mockk()
        isUserLoggedInUseCase = mockk()
        isFirstTimeLaunchAppUseCase = mockk()
        getAppThemeUseCase = mockk()
        getAppLanguageUseCase = mockk()
        syncSavedMoviesUseCase = mockk()

        coEvery { isUserLoggedInUseCase.invoke() } returns true
        coEvery { isFirstTimeLaunchAppUseCase() } returns true
        coEvery { getAppThemeUseCase() } returns flowOf(true)
        coEvery { getAppLanguageUseCase() } returns flowOf("en")
        coEvery { syncSavedMoviesUseCase() } returns Unit
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): MainViewModel {
        return MainViewModel(
            isUserLoggedInUseCase,
            getAppThemeUseCase,
            getAppLanguageUseCase,
            isFirstTimeLaunchAppUseCase,
            syncSavedMoviesUseCase
        )
    }

    @Test
    fun `isUserLoggedIn should be called when ViewModel starts`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase.invoke() } returns true

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { isUserLoggedInUseCase.invoke() }
    }

    @Test
    fun `isLoading should remain true when authentication check is not yet complete`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase.invoke() } coAnswers {
            delay(1000)
            true
        }

        coEvery { isFirstTimeLaunchAppUseCase() } coAnswers {
            delay(1000)
            true
        }

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceTimeBy(500)

        // Then
        assertThat(viewModel.uiState.value.isLoading).isTrue()
    }


    @Test
    fun `should update isAppInDarkTheme when getAppThemeUseCase returns true`() = runTest {
        // Given
        coEvery { getAppThemeUseCase() } returns flowOf(true)

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isAppInDarkTheme).isTrue()
    }

    @Test
    fun `should update appLanguage when getAppLanguageUseCase returns language`() = runTest {
        // Given
        coEvery { getAppLanguageUseCase() } returns flowOf("ar")

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.appLanguage).isEqualTo("ar")
    }

    @Test
    fun `should call syncSavedMoviesUseCase when user is logged in`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase.invoke() } returns true
        coEvery { syncSavedMoviesUseCase() } returns Unit

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { syncSavedMoviesUseCase() }
    }

    @Test
    fun `should not call syncSavedMoviesUseCase when user is not logged in`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase.invoke() } returns false

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { syncSavedMoviesUseCase() }
    }

    @Test
    fun `should set isLoading to false when all checks are complete`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase.invoke() } returns true
        coEvery { isFirstTimeLaunchAppUseCase() } returns false
        coEvery { getAppThemeUseCase() } returns flowOf(true)
        coEvery { getAppLanguageUseCase() } returns flowOf("en")
        coEvery { syncSavedMoviesUseCase() } returns Unit

        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}