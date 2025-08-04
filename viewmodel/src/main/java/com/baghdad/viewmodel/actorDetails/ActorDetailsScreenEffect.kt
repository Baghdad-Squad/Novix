package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class ActorDetailsScreenEffect : BaseUiEffect {
    data object NavigateBack : ActorDetailsScreenEffect()
    data object NavigateToLogin : ActorDetailsScreenEffect()
    data class NavigateToActorGallery(val actorId: Long) : ActorDetailsScreenEffect()
    data class NavigateToActorTopMoviePicks(val actorId: Long) : ActorDetailsScreenEffect()
    data class NavigateToActorTopTvShowPicks(val actorId: Long) : ActorDetailsScreenEffect()
    data class NavigateToMovieDetails(val movieId: Long) : ActorDetailsScreenEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : ActorDetailsScreenEffect()
}