package com.baghdad.ui.navigation.graph.authentication

sealed interface AuthenticationNavEvent {
    data object NavigateToHome : AuthenticationNavEvent
}