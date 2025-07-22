package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface PeopleRoute: Route {
    @Serializable
    data object PeopleScreen : PeopleRoute
}