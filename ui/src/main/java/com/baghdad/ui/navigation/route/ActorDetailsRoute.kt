package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface ActorDetailsRoute : Route {
    @Serializable
    data object ActorDetailsScreen : Route

    @Serializable
    data class ActorGalleryScreen(val actorId: Long) : Route

    @Serializable
    data class ActorTopMoviePicksScreen(val actorId: Long) : Route

    @Serializable
    data class ActorTopTvShowPicksScreen(val actorId: Long): Route
}