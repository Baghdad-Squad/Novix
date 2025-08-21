package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface MyListsRoute : Route {
    @Serializable
    data class MyListsScreen(val isDeleteSuccess: Boolean = false) : MyListsRoute

    @Serializable
    data class ListDetailsScreen(val listId: Long) : MyListsRoute
}