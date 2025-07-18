package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface EpisodeDetailsScreenEffect : BaseUiEffect {
    data object NavigateBack : EpisodeDetailsScreenEffect
    data object NavigateToLogin : EpisodeDetailsScreenEffect
    data class NavigateToCategoryTvShows(val categoryId: Long) : EpisodeDetailsScreenEffect
    data class NavigateToActorDetails(val actorId: Long) : EpisodeDetailsScreenEffect
}