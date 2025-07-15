package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface MyListsRoute : Route {
    @Serializable
    data object MyListsScreen : MyListsRoute

    @Serializable
    data class ListDetailsScreen(val listId: Long) : MyListsRoute
}