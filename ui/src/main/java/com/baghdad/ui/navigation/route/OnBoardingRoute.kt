package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface OnBoardingRoute : Route {
    @Serializable
    data object OnBoardingScreen : OnBoardingRoute

    @Serializable
    data object WelcomeScreen : OnBoardingRoute
}