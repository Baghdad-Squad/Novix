package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface ContinueWatchingRoute : Route {
    @Serializable
    data object CurrentWatchingScreen : ContinueWatchingRoute
    @Serializable
    data class MovieDetailsGraph(val movieId: Int) : ContinueWatchingRoute
    @Serializable
    data class TvShowDetailsGraph(val tvShowId: Int) : ContinueWatchingRoute

}