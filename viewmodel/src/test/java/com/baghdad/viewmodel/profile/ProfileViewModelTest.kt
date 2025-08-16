// package com.baghdad.viewmodel.profile
//
// import app.cash.turbine.test
// import com.baghdad.domain.exception.NoInternetException
// import com.baghdad.domain.usecase.appConfigurations.GetAppLanguageUseCase
// import com.baghdad.domain.usecase.appConfigurations.GetAppThemeUseCase
// import com.baghdad.domain.usecase.appConfigurations.SetAppLanguageUseCase
// import com.baghdad.domain.usecase.appConfigurations.SetAppThemeUseCase
// import com.baghdad.domain.usecase.login.GetUserInfo
// import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
// import com.baghdad.domain.usecase.login.LogOutUseCase
// import com.baghdad.entity.user.User
// import com.google.common.truth.Truth.assertThat
// import io.mockk.coEvery
// import io.mockk.coVerify
// import io.mockk.mockk
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlinx.coroutines.flow.flowOf
// import kotlinx.coroutines.test.StandardTestDispatcher
// import kotlinx.coroutines.test.advanceUntilIdle
// import kotlinx.coroutines.test.resetMain
// import kotlinx.coroutines.test.runTest
// import kotlinx.coroutines.test.setMain
// import org.junit.jupiter.api.AfterEach
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
//
// @OptIn(ExperimentalCoroutinesApi::class)
//
// class ProfileViewModelTest {
//    private val logOutUseCase = mockk<LogOutUseCase>()
//    private val getUserInfo = mockk<GetUserInfo>()
//    private val isUserLoggedInUseCase = mockk<IsUserLoggedInUseCase>()
//    private val setAppThemeUseCase = mockk<SetAppThemeUseCase>()
//    private val setAppLanguageUseCase = mockk<SetAppLanguageUseCase>()
//    private val getAppThemeUseCase = mockk<GetAppThemeUseCase>()
//    private val getAppLanguageUseCase = mockk<GetAppLanguageUseCase>()
//    private val testDispatcher = StandardTestDispatcher()
//    private lateinit var viewModel: ProfileViewModel
//
//    @BeforeEach
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @AfterEach
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `createViewModel should set isUserLoggedIn to true when user is logged in`() = runTest {
//        coEvery { isUserLoggedInUseCase.invoke() } returns true
//
//        viewModel = createViewModel()
//        advanceUntilIdle()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.isUserLoggedIn).isTrue()
//        }
//    }
//
//    @Test
//    fun `createViewModel should set isUserLoggedIn to false when user is not logged in`() =
//        runTest {
//            coEvery { isUserLoggedInUseCase.invoke() } returns false
//
//            viewModel = createViewModel()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.isUserLoggedIn).isFalse()
//            }
//        }
//
//    @Test
//    fun `createViewModel should fetch user info when user is logged in`() = runTest {
//        coEvery { isUserLoggedInUseCase.invoke() } returns true
//        coEvery { getUserInfo.invoke() } returns mockUser
//
//        viewModel = createViewModel()
//        advanceUntilIdle()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.userInfo).isEqualTo(mockUser.toUIState())
//        }
//    }
//
//    @Test
//    fun `createViewModel should not fetch user info when user is not logged in`() = runTest {
//        coEvery { isUserLoggedInUseCase.invoke() } returns false
//        coEvery { getUserInfo.invoke() } returns null
//
//        viewModel = createViewModel()
//        advanceUntilIdle()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.userInfo).isEqualTo(ProfileScreenState.User())
//        }
//    }
//
//    @Test
//    fun `createViewModel should load English language when getAppLanguageUseCase returns English`() =
//        runTest {
//            coEvery { getAppLanguageUseCase.invoke() } returns flowOf("en")
//
//            viewModel = createViewModel()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.userSettings.language).isEqualTo(ProfileScreenState.LanguagePreferences.ENGLISH)
//            }
//        }
//
//    @Test
//    fun `createViewModel should load Arabic language when getAppLanguageUseCase returns Arabic`() =
//        runTest {
//            coEvery { getAppLanguageUseCase.invoke() } returns flowOf("ar")
//
//            viewModel = createViewModel()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.userSettings.language).isEqualTo(ProfileScreenState.LanguagePreferences.ARABIC)
//            }
//        }
//
//    @Test
//    fun `createViewModel should set theme to dark when getAppThemeUseCase returns true`() =
//        runTest {
//            coEvery { getAppThemeUseCase.invoke() } returns flowOf(true)
//
//            viewModel = createViewModel()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.userSettings.appearance).isEqualTo(ProfileScreenState.ThemePreferences.DARK)
//            }
//        }
//
//    @Test
//    fun `createViewModel should set theme to light when getAppThemeUseCase returns false`() =
//        runTest {
//            coEvery { getAppThemeUseCase.invoke() } returns flowOf(false)
//
//            viewModel = createViewModel()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.userSettings.appearance).isEqualTo(ProfileScreenState.ThemePreferences.LIGHT)
//            }
//        }
//
//    @Test
//    fun `onWatchingHistoryClick should navigate to watching history when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onWatchingHistoryClick()
//
//        viewModel.uiEffect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(ProfileEffect.NavigateToWatchingHistory)
//        }
//    }
//
//    @Test
//    fun `onMyRatingClick should navigate to my ratings when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onMyRatingClick()
//
//        viewModel.uiEffect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(ProfileEffect.NavigateToMyRatings)
//        }
//    }
//
//    @Test
//    fun `onChangePasswordClick should navigate to change password when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onChangePasswordClick()
//
//        viewModel.uiEffect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(ProfileEffect.NavigateToChangePassword)
//        }
//    }
//
//    @Test
//    fun `onAppearanceClick should show theme bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onAppearanceClick()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.themeBottomSheetState.isVisible).isTrue()
//        }
//    }
//
//    @Test
//    fun `onLanguageClick should show language bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLanguageClick()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.languageBottomSheetState.isVisible).isTrue()
//        }
//    }
//
//    @Test
//    fun `onLoginClick should navigate to login when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLoginClick()
//
//        viewModel.uiEffect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(ProfileEffect.NavigateToLogin)
//        }
//    }
//
//    @Test
//    fun `onAppearanceChanged should update theme in bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onAppearanceChanged(ProfileScreenState.ThemePreferences.DARK)
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.themeBottomSheetState.currentTheme).isEqualTo(ProfileScreenState.ThemePreferences.DARK)
//        }
//    }
//
//    @Test
//    fun `onAppearanceConfirmed should save theme and hide bottom sheet when successful`() =
//        runTest {
//            coEvery { setAppThemeUseCase.invoke(any()) } returns Unit
//            viewModel = createViewModel()
//
//            viewModel.onAppearanceConfirmed()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.themeBottomSheetState.isVisible).isFalse()
//            }
//            coVerify { setAppThemeUseCase.invoke(any()) }
//        }
//
//    @Test
//    fun `onLanguageConfirmed should save language and hide bottom sheet when successful`() =
//        runTest {
//            coEvery { setAppLanguageUseCase.invoke(any()) } returns Unit
//            viewModel = createViewModel()
//
//            viewModel.onLanguageConfirmed()
//            advanceUntilIdle()
//
//            viewModel.uiState.test {
//                val state = awaitItem()
//                assertThat(state.languageBottomSheetState.isVisible).isFalse()
//            }
//            coVerify { setAppLanguageUseCase.invoke(any()) }
//        }
//
//    @Test
//    fun `onLanguageChanged should update language in bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLanguageChanged(ProfileScreenState.LanguagePreferences.ARABIC)
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.languageBottomSheetState.currentLanguage).isEqualTo(ProfileScreenState.LanguagePreferences.ARABIC)
//        }
//    }
//
//    @Test
//    fun `onLogOutClick should show logout bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLogOutClick()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.logoutBottomSheetState.isVisible).isTrue()
//        }
//    }
//
//    @Test
//    fun `onLogoutDialogDismissed should hide logout bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLogoutDialogDismissed()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.logoutBottomSheetState.isVisible).isFalse()
//        }
//    }
//
//    @Test
//    fun `onAppearanceDialogDismissed should hide theme bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onAppearanceDialogDismissed()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.themeBottomSheetState.isVisible).isFalse()
//        }
//    }
//
//    @Test
//    fun `onLanguageDialogDismissed should hide language bottom sheet when called`() = runTest {
//        viewModel = createViewModel()
//
//        viewModel.onLanguageDialogDismissed()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertThat(state.languageBottomSheetState.isVisible).isFalse()
//        }
//    }
//
//    @Test
//    fun `onLogOutConfirmed should call logOutUseCase when executed`() = runTest {
//        viewModel = createViewModel()
//        coEvery { logOutUseCase.invoke() } returns true
//
//        viewModel.onLogOutConfirmed()
//        advanceUntilIdle()
//
//        coVerify(exactly = 1) { logOutUseCase.invoke() }
//    }
//
//    @Test
//    fun `onLogOutConfirmed should not navigate to login when error occurs`() = runTest {
//        viewModel = createViewModel()
//        coEvery { logOutUseCase.invoke() } throws NoInternetException()
//
//        viewModel.onLogOutConfirmed()
//        advanceUntilIdle()
//
//        viewModel.uiEffect.test {
//            expectNoEvents()
//        }
//    }
//
//    @Test
//    fun `onSnackBarActionLabelClick should call onLogOutConfirmed when executed`() = runTest {
//        coEvery { logOutUseCase.invoke() } returns true
//        viewModel = createViewModel()
//
//        viewModel.onSnackBarActionLabelClick()
//        advanceUntilIdle()
//
//        coVerify(exactly = 1) { logOutUseCase.invoke() }
//    }
//
//    companion object {
//        val mockUser = User(
//            id = 1L,
//            userName = "testuser",
//            imageUrl = "https://example.com/avatar.jpg"
//        )
//    }
//
//    private fun createViewModel(): ProfileViewModel {
//        return ProfileViewModel(
//            logOutUseCase = logOutUseCase,
//            getUserInfo = getUserInfo,
//            isUserLoggedInUseCase = isUserLoggedInUseCase,
//            setAppThemeUseCase = setAppThemeUseCase,
//            setAppLanguageUseCase = setAppLanguageUseCase,
//            getAppThemeUseCase = getAppThemeUseCase,
//            getAppLanguageUseCase = getAppLanguageUseCase,
//            ioDispatcher = testDispatcher
//        )
//    }
// }
