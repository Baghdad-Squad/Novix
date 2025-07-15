package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface ActorDetailsRoute : Route {
    @Serializable
    data object ActorDetailsScreen : Route

    @Serializable
    data object ActorGalleryScreen : Route

    @Serializable
    data object ActorTopMoviePicksScreen : Route

    @Serializable
    data object ActorTopTvShowPicksScreen : Route
}