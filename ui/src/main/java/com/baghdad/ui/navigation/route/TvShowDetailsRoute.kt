package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface TvShowDetailsRoute : Route {
    @Serializable
    data object TvShowDetailsScreen : Route

    @Serializable
    data class EpisodeDetailsScreen(val tvShowId: Long ,val seasonNumber: Int, val episodeNumber: Int) : Route
}