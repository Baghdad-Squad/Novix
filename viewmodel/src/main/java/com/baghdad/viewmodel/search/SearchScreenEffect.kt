package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.base.BaseUiEffect


sealed class SearchScreenEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long) : SearchScreenEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchScreenEffect()
    data class NavigateToActorDetails(val actorId: Long) : SearchScreenEffect()
    data class NavigateToRecentlyViewedDetails(val mediaId: Long) : SearchScreenEffect()
}