package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TvShowDetailsScreenEffect : BaseUiEffect {
    data object NavigateBack : TvShowDetailsScreenEffect()
    data object NavigateToLogin : TvShowDetailsScreenEffect()
    data class NavigateToActorDetails(val actorId: Long) : TvShowDetailsScreenEffect()
    data class NavigateToEpisodeDetails(val seasonNumber: Int, val episodeNumber: Int) :
        TvShowDetailsScreenEffect()
    data class NavigateToGenreScreen(val genreId: Long) : TvShowDetailsScreenEffect()
    data class NavigateToReviews(val tvShowId: Long) : TvShowDetailsScreenEffect()

}