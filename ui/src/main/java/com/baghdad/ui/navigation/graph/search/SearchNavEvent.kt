package com.baghdad.ui.navigation.graph.search

sealed interface SearchNavEvent {
    data object NavigateToLogin : SearchNavEvent
    data class NavigateToActorDetails(val actorId: Long) : SearchNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : SearchNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchNavEvent
}