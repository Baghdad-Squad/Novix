package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface SearchRoute : Route {
    @Serializable
    data object SearchScreen : SearchRoute
}