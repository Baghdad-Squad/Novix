package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface MyAccountRoute : Route {
    @Serializable
    data object MyAccountScreen : MyAccountRoute

    @Serializable
    data object WatchingHistoryScreen : MyAccountRoute

    @Serializable
    data object MyRatingsScreen: MyAccountRoute

    @Serializable
    data object ResetPasswordScreen : MyAccountRoute


}