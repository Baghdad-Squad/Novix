package com.baghdad.ui.navigation.graph.tvShowDetails

sealed interface TvShowDetailsNavEvent {
    data object NavigateBack : TvShowDetailsNavEvent
    data object NavigateToLogin : TvShowDetailsNavEvent
    data class NavigateToReviews(val movieId: Long) : TvShowDetailsNavEvent
    data class NavigateToActorDetails(val actorId: Long) : TvShowDetailsNavEvent
    data class NavigateToCategoryTvShows(val categoryId: Long) : TvShowDetailsNavEvent
    data class NavigateToEpisodeDetails(val tvShowId: Long, val seasonNumber: Int, val episodeNumber: Int) :
        TvShowDetailsNavEvent
}