package com.baghdad.viewmodel.welcome

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface WelcomeEffect : BaseUiEffect {
    data object NavigateToLogin : WelcomeEffect
    data object NavigateToContinueAsGuest : WelcomeEffect
}