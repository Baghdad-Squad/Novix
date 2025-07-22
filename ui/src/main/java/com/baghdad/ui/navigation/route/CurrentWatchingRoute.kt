package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface CurrentWatchingRoute : Route {
    @Serializable
    data object CurrentWatchingScreen : CurrentWatchingRoute
}