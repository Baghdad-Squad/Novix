package com.baghdad.ui.navigation.graph.home

sealed interface HomeNavEvent {
    data object NavigateBack : HomeNavEvent
    data object NavigateToLogin : HomeNavEvent
    data object NavigateToActors : HomeNavEvent
    data object NavigateToMovies : HomeNavEvent
    data object NavigateToTvShows : HomeNavEvent
    data object NavigateToPopularMovies : HomeNavEvent
    data object NavigateToTopRatingMovies : HomeNavEvent
    data object NavigateToContinueWatching : HomeNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : HomeNavEvent
    data class NavigateToActorDetails(val actorId: Long) : HomeNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : HomeNavEvent
}