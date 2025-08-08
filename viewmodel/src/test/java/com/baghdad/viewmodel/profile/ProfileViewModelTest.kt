package com.baghdad.viewmodel.profile

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.GetCurrentLoggedInUserUseCase
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.login.LogOutUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.GetAppThemeUseCase
import com.baghdad.domain.usecase.userPreferences.SetAppLanguageUseCase
import com.baghdad.domain.usecase.userPreferences.SetAppThemeUseCase
import com.baghdad.entity.User
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)

class ProfileViewModelTest {
    private lateinit var logOutUseCase: LogOutUseCase
    private lateinit var getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    private lateinit var setAppThemeUseCase: SetAppThemeUseCase
    private lateinit var setAppLanguageUseCase: SetAppLanguageUseCase
    private lateinit var getAppThemeUseCase: GetAppThemeUseCase
    private lateinit var getAppLanguageUseCase: GetAppLanguageUseCase
    private lateinit var profileViewModel: ProfileViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val mockUser = User(
        id = 1L,
        userName = "testuser",
        imageUrl = "https://example.com/avatar.jpg"
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        logOutUseCase = mockk(relaxed = true)
        getCurrentLoggedInUserUseCase = mockk(relaxed = true)
        isUserLoggedInUseCase = mockk(relaxed = true)
        setAppThemeUseCase = mockk(relaxed = true)
        setAppLanguageUseCase = mockk(relaxed = true)
        getAppThemeUseCase = mockk(relaxed = true)
        getAppLanguageUseCase = mockk(relaxed = true)

        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { getCurrentLoggedInUserUseCase() } returns mockUser
        coEvery { getAppThemeUseCase() } returns flowOf(false)
        coEvery { getAppLanguageUseCase() } returns flowOf("en")

        profileViewModel = ProfileViewModel(
            logOutUseCase = logOutUseCase,
            getCurrentLoggedInUserUseCase = getCurrentLoggedInUserUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            setAppThemeUseCase = setAppThemeUseCase,
            setAppLanguageUseCase = setAppLanguageUseCase,
            getAppThemeUseCase = getAppThemeUseCase,
            getAppLanguageUseCase = getAppLanguageUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `onWatchingHistoryClick should navigate to watching history when clicked`() = runTest {
        // Given
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onWatchingHistoryClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToWatchingHistory).isTrue()
        job.cancel()
    }

    @Test
    fun `onMyRatingClick should navigate to my ratings when clicked`() = runTest {
        // Given
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onMyRatingClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToMyRatings).isTrue()
        job.cancel()
    }

    @Test
    fun `onChangePasswordClick should navigate to change password when clicked`() = runTest {
        // Given
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onChangePasswordClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToChangePassword).isTrue()
        job.cancel()
    }

    @Test
    fun `onLoginClick should navigate to login when clicked`() = runTest {
        // Given
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onLoginClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToLogin).isTrue()
        job.cancel()
    }

    @Test
    fun `onAppearanceClick should show theme bottom sheet when clicked`() = runTest {
        // When
        profileViewModel.onAppearanceClick()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.themeBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onLanguageClick should show language bottom sheet when clicked`() = runTest {
        // When
        profileViewModel.onLanguageClick()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.languageBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onLogOutClick should show logout bottom sheet when clicked`() = runTest {
        // When
        profileViewModel.onLogOutClick()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.logoutBottomSheetState.isVisible).isTrue()
    }

    @Test
    fun `onAppearanceChanged should update current theme when theme changed`() = runTest {
        // Given
        val newTheme = ThemePreferences.DARK

        // When
        profileViewModel.onAppearanceChanged(newTheme)

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.themeBottomSheetState.currentTheme).isEqualTo(newTheme)
    }

    @Test
    fun `onLanguageChanged should update current language when language changed`() = runTest {
        // Given
        val newLanguage = LanguagePreferences.ARABIC

        // When
        profileViewModel.onLanguageChanged(newLanguage)

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.languageBottomSheetState.currentLanguage).isEqualTo(newLanguage)
    }

    @Test
    fun `onLogoutDialogDismissed should hide logout bottom sheet when dismissed`() = runTest {
        // Given
        profileViewModel.onLogOutClick() // Show first

        // When
        profileViewModel.onLogoutDialogDismissed()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.logoutBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onAppearanceDialogDismissed should hide theme bottom sheet when dismissed`() = runTest {
        // Given
        profileViewModel.onAppearanceClick()

        // When
        profileViewModel.onAppearanceDialogDismissed()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.themeBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onLanguageDialogDismissed should hide language bottom sheet when dismissed`() = runTest {
        // Given
        profileViewModel.onLanguageClick()

        // When
        profileViewModel.onLanguageDialogDismissed()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.languageBottomSheetState.isVisible).isFalse()
    }

    @Test
    fun `onLogOutConfirmed should navigate to login when logout successful`() = runTest {
        // Given
        coEvery { logOutUseCase() } returns true
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onLogOutConfirmed()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToLogin).isTrue()
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.isUserLoggedIn).isTrue()
        job.cancel()
    }

    @Test
    fun `onLogOutConfirmed should show no internet snackBar when NoInternetException thrown`() = runTest {
        // Given
        coEvery { logOutUseCase() } throws NoInternetException()
        val emittedSnackBarMessages = mutableListOf<BaseSnackBarMessage>()

        val job = launch {
            profileViewModel.snackBarState.collect {
                emittedSnackBarMessages.add(it.message)
            }
        }

        // When
        profileViewModel.onLogOutConfirmed()
        advanceUntilIdle()

        // Then
        assertThat(emittedSnackBarMessages).contains(BaseSnackBarMessage.NoInternetException)
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.logoutBottomSheetState.isVisible).isFalse()
        job.cancel()
    }

    @Test
    fun `onSnackBarActionLabelClick should retry logout when action clicked`() = runTest {
        // Given
        coEvery { logOutUseCase() } returns true
        var receivedEffect: ProfileEffect? = null
        val job = launch {
            profileViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        // When
        profileViewModel.onSnackBarActionLabelClick()
        advanceUntilIdle()

        // Then
        assertThat(receivedEffect is ProfileEffect.NavigateToLogin).isTrue()
        job.cancel()
    }

    @Test
    fun `getAppTheme should update theme state when theme retrieved successfully`() = runTest {
        // Given
        coEvery { getAppThemeUseCase() } returns flowOf(true)

        // When
        profileViewModel = ProfileViewModel(
            logOutUseCase = logOutUseCase,
            getCurrentLoggedInUserUseCase = getCurrentLoggedInUserUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            setAppThemeUseCase = setAppThemeUseCase,
            setAppLanguageUseCase = setAppLanguageUseCase,
            getAppThemeUseCase = getAppThemeUseCase,
            getAppLanguageUseCase = getAppLanguageUseCase,
            ioDispatcher = testDispatcher
        )
        advanceUntilIdle()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.userSettings.appearance).isEqualTo(ThemePreferences.DARK)
        assertThat(currentState.themeBottomSheetState.currentTheme).isEqualTo(ThemePreferences.DARK)
    }

    @Test
    fun `getAppLanguage should update language state when language retrieved successfully`() = runTest {
        // Given
        coEvery { getAppLanguageUseCase() } returns flowOf("ar")

        // When
        profileViewModel = ProfileViewModel(
            logOutUseCase = logOutUseCase,
            getCurrentLoggedInUserUseCase = getCurrentLoggedInUserUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            setAppThemeUseCase = setAppThemeUseCase,
            setAppLanguageUseCase = setAppLanguageUseCase,
            getAppThemeUseCase = getAppThemeUseCase,
            getAppLanguageUseCase = getAppLanguageUseCase,
            ioDispatcher = testDispatcher
        )
        advanceUntilIdle()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.userSettings.language).isEqualTo(LanguagePreferences.fromLanguageCode("ar"))
        assertThat(currentState.languageBottomSheetState.currentLanguage).isEqualTo(LanguagePreferences.fromLanguageCode("ar"))
    }

    @Test
    fun `checkIsUserLoggedIn should update user logged in state when check successful`() = runTest {
        // Given
        coEvery { isUserLoggedInUseCase() } returns false

        // When
        profileViewModel = ProfileViewModel(
            logOutUseCase = logOutUseCase,
            getCurrentLoggedInUserUseCase = getCurrentLoggedInUserUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            setAppThemeUseCase = setAppThemeUseCase,
            setAppLanguageUseCase = setAppLanguageUseCase,
            getAppThemeUseCase = getAppThemeUseCase,
            getAppLanguageUseCase = getAppLanguageUseCase,
            ioDispatcher = testDispatcher
        )
        advanceUntilIdle()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.isUserLoggedIn).isFalse()
    }

    @Test
    fun `onAppearanceConfirmed should call setAppThemeUseCase when appearance confirmed`() = runTest {
        // Given
        profileViewModel.onAppearanceClick()
        profileViewModel.onAppearanceChanged(ThemePreferences.DARK)

        // When
        profileViewModel.onAppearanceConfirmed()
        advanceUntilIdle()

        // Then
        io.mockk.coVerify { setAppThemeUseCase(true) }
    }

    @Test
    fun `onLanguageConfirmed should call setAppLanguageUseCase when language confirmed`() = runTest {
        // Given
        profileViewModel.onLanguageClick()
        profileViewModel.onLanguageChanged(LanguagePreferences.ARABIC)

        // When
        profileViewModel.onLanguageConfirmed()
        advanceUntilIdle()

        // Then
        io.mockk.coVerify { setAppLanguageUseCase("ar") }
    }

    @Test
    fun `getUserInfo should update user info when user retrieved successfully`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        val currentState = profileViewModel.uiState.value
        assertThat(currentState.userInfo).isNotNull()
    }
}