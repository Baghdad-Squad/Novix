package com.baghdad.viewmodel.profile

interface ProfileInteractionListener {
    fun onSnackBarActionLabelClick()
    fun onWatchingHistoryClick()
    fun onMyRatingClick()
    fun onContentRestrictionClick()
    fun onChangePasswordClick()
    fun onAppearanceClick()
    fun onLogoutDialogDismissed()
    fun onAppearanceDialogDismissed()
    fun onLanguageDialogDismissed()
    fun onLanguageClick()
    fun onLogOutClick()
    fun onLogOutConfirmed()
    fun onLoginClick()
    fun onAppearanceChanged(theme: ProfileScreenState.ThemePreferences)
    fun onAppearanceConfirmed()
    fun onLanguageConfirmed()
    fun onLanguageChanged(language: ProfileScreenState.LanguagePreferences)
}