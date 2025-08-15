package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.profile.ContentRestriction

data class MainState(
    val isFirstTimeUser: Boolean? = null,
    val isLoggedIn: Boolean? = null,
    val isAppInDarkTheme: Boolean? = true,
    val appLanguage: String = "ar",
    val isLoading: Boolean = true,
    val contentRestriction: ContentRestriction = ContentRestriction.STRICT,
) : BaseUiState