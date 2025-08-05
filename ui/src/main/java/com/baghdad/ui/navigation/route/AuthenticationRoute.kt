package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface AuthenticationRoute : Route {
    @Serializable
    data object WelcomeScreen : OnBoardingRoute

    @Serializable
    data object LoginScreen : AuthenticationRoute

    @Serializable
    data object SignUpScreen : AuthenticationRoute

    @Serializable
    data object ForgotPasswordScreen : AuthenticationRoute
}