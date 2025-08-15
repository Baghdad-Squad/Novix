package com.baghdad.viewmodel.profile

import androidx.annotation.StringRes
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseUiState

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val userInfo: User = User(),
    val userSettings: UserSettings = UserSettings(),
    val languageBottomSheetState: LanguageBottomSheetState = LanguageBottomSheetState(),
    val themeBottomSheetState: ThemeBottomSheetState = ThemeBottomSheetState(),
    val logoutBottomSheetState: LogoutBottomSheetState = LogoutBottomSheetState(),
    val contentRestrictionBottomSheetState: ContentRestrictionBottomSheetState = ContentRestrictionBottomSheetState()
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

    data class ContentRestrictionBottomSheetState(
        val isVisible: Boolean = false,
        val currentRestriction: ContentRestriction = ContentRestriction.STRICT,
    )

    enum class LanguagePreferences(
        @StringRes val title: Int,
        val languageCode: String = "",
    ) {
        ENGLISH(R.string.english, "en"),
        ARABIC(R.string.arabic, "ar");

        companion object {
            fun fromLanguageCode(code: String): LanguagePreferences {
                return entries.firstOrNull { it.languageCode == code } ?: ARABIC
            }
        }
    }

    enum class ThemePreferences(@StringRes val title: Int, val isDark: Boolean = false) {
        DARK(R.string.dark, true),
        LIGHT(R.string.light, false)
    }
}

enum class ContentRestriction(@StringRes val title: Int, @StringRes val description: Int) {
    STRICT(R.string.content_restriction_strict, R.string.content_restriction_strict_description),
    MODERATE(
        R.string.content_restriction_moderate,
        R.string.content_restriction_moderate_description
    ),
    NONE(R.string.content_restriction_none, R.string.content_restriction_none_description)
}