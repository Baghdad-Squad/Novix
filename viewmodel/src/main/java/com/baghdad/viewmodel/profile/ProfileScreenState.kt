package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseUiState

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val userInfo: User = User(),
    val userSettings: UserSettings = UserSettings(),
    val languageBottomSheetState: LanguageBottomSheetState = LanguageBottomSheetState(),
    val themeBottomSheetState: ThemeBottomSheetState = ThemeBottomSheetState(),
    val logoutBottomSheetState: LogoutBottomSheetState = LogoutBottomSheetState(),
) : BaseUiState {

    data class User(
        val userName: String = "",
        val imageUrl: String = "",
    )

    data class UserSettings(
        val appearance: ThemePreferences = ThemePreferences.DARK,
        val language: LanguagePreferences = LanguagePreferences.ARABIC,
    )

    data class LanguageBottomSheetState(
        val isVisible: Boolean = false,
        val currentLanguage: LanguagePreferences = LanguagePreferences.ARABIC,
    )

    data class ThemeBottomSheetState(
        val isVisible: Boolean = false,
        val currentTheme: ThemePreferences = ThemePreferences.DARK,
    )

    data class LogoutBottomSheetState(
        val isVisible: Boolean = false,
    )
}