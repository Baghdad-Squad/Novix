package com.baghdad.ui.navigation.graph.myLists

sealed interface MyListsNavEvent {
    data object NavigateToMyLists : MyListsNavEvent
    data object NavigateToLogin : MyListsNavEvent
    data class NavigateToListDetails(val listId: Long) : MyListsNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : MyListsNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : MyListsNavEvent
}