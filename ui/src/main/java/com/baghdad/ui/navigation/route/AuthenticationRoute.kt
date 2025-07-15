package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface AuthenticationRoute : Route {
    @Serializable
    data object LoginScreen : AuthenticationRoute
}