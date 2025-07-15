package com.baghdad.ui.navigation.graph.myAccount

sealed interface MyAccountNavEvent {
    data object NavigateBack : MyAccountNavEvent
    data object NavigateToLogin : MyAccountNavEvent
    data object NavigateToMyRatings : MyAccountNavEvent
    data object NavigateToWatchingHistory : MyAccountNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : MyAccountNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : MyAccountNavEvent
}