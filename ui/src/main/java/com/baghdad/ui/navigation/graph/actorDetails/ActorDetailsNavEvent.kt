package com.baghdad.ui.navigation.graph.actorDetails

sealed interface ActorDetailsNavEvent {
    data object NavigateBack : ActorDetailsNavEvent
    data object NavigateToLogin : ActorDetailsNavEvent
    data object NavigateToActorGallery : ActorDetailsNavEvent
    data object NavigateToActorTopMoviePicks : ActorDetailsNavEvent
    data object NavigateToActorTopTvShowPicks : ActorDetailsNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : ActorDetailsNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : ActorDetailsNavEvent

}