package com.baghdad.ui.navigation.graph.currentWatching

sealed interface ContinueWatchingNavEvent {
    data object NavigateBack : ContinueWatchingNavEvent
    data object NavigateToLogin : ContinueWatchingNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : ContinueWatchingNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : ContinueWatchingNavEvent
}