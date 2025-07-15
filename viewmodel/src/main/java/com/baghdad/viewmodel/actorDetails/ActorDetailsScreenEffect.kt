package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class ActorDetailsScreenEffect : BaseUiEffect {
    data object NavigateBack : ActorDetailsScreenEffect()
    data object NavigateToLogin : ActorDetailsScreenEffect()
    data object NavigateToActorGallery : ActorDetailsScreenEffect()
    data object NavigateToActorTopMoviePicks : ActorDetailsScreenEffect()
    data object NavigateToActorTopTvShowPicks : ActorDetailsScreenEffect()
    data class NavigateToMovieDetails(val movieId: Long) : ActorDetailsScreenEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : ActorDetailsScreenEffect()
}