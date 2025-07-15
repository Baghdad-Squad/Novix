package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface MovieDetailsRoute : Route {
    @Serializable
    data object MovieDetailsScreen : Route
}