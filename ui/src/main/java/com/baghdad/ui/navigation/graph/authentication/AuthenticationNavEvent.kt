package com.baghdad.ui.navigation.graph.authentication

sealed interface AuthenticationNavEvent {
    data object NavigateToHome : AuthenticationNavEvent
    data object NavigateToRegister : AuthenticationNavEvent
    data object NavigateToForgotPassword : AuthenticationNavEvent
    data object NavigateBack : AuthenticationNavEvent
}