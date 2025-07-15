package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface ReviewsRoute : Route {
    @Serializable
    data object ReviewsScreen : ReviewsRoute
}