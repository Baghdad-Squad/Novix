package com.baghdad.viewmodel.main

import com.baghdad.domain.usecase.appConfigurations.GetAppLanguageUseCase
import com.baghdad.domain.usecase.appConfigurations.GetAppThemeUseCase
import com.baghdad.domain.usecase.appConfigurations.IsFirstTimeLaunchAppUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.SyncSavedMoviesUseCase
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
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk()
    private val isFirstTimeLaunchAppUseCase: IsFirstTimeLaunchAppUseCase = mockk()
    private val getAppThemeUseCase: GetAppThemeUseCase = mockk()
    private val getAppLanguageUseCase: GetAppLanguageUseCase = mockk()
    private val syncSavedMoviesUseCase: SyncSavedMoviesUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

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
        coEvery { isUserLoggedInUseCase.invoke() } returns true

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { isUserLoggedInUseCase.invoke() }
    }

    @Test
    fun `isLoading should remain true when authentication check is not yet complete`() = runTest {
        coEvery { isUserLoggedInUseCase.invoke() } coAnswers {
            delay(1000)
            true
        }

        coEvery { isFirstTimeLaunchAppUseCase() } coAnswers {
            delay(1000)
            true
        }

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceTimeBy(500)

        assertThat(viewModel.uiState.value.isLoading).isTrue()
    }


    @Test
    fun `should update isAppInDarkTheme when getAppThemeUseCase returns true`() = runTest {
        coEvery { getAppThemeUseCase() } returns flowOf(true)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.isAppInDarkTheme).isTrue()
    }

    @Test
    fun `should update appLanguage when getAppLanguageUseCase returns language`() = runTest {
        coEvery { getAppLanguageUseCase() } returns flowOf("ar")

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.appLanguage).isEqualTo("ar")
    }

    @Test
    fun `should call syncSavedMoviesUseCase when user is logged in`() = runTest {
        coEvery { isUserLoggedInUseCase.invoke() } returns true
        coEvery { syncSavedMoviesUseCase() } returns Unit

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { syncSavedMoviesUseCase() }
    }

    @Test
    fun `should not call syncSavedMoviesUseCase when user is not logged in`() = runTest {
        coEvery { isUserLoggedInUseCase.invoke() } returns false

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { syncSavedMoviesUseCase() }
    }

    @Test
    fun `should set isLoading to false when all checks are complete`() = runTest {
        coEvery { isUserLoggedInUseCase.invoke() } returns true
        coEvery { isFirstTimeLaunchAppUseCase() } returns false
        coEvery { getAppThemeUseCase() } returns flowOf(true)
        coEvery { getAppLanguageUseCase() } returns flowOf("en")
        coEvery { syncSavedMoviesUseCase() } returns Unit

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}