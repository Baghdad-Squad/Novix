package com.baghdad.ui.navigation.graph.actorDetails

sealed interface ActorDetailsNavEvent {

    data object NavigateBack : ActorDetailsNavEvent
    data object NavigateToLogin : ActorDetailsNavEvent
    data class NavigateToActorGallery(val actorId: Long) : ActorDetailsNavEvent
    data class NavigateToActorTopMoviePicks(val actorId: Long) : ActorDetailsNavEvent
    data class NavigateToActorTopTvShowPicks(val actorId: Long) : ActorDetailsNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : ActorDetailsNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : ActorDetailsNavEvent

}