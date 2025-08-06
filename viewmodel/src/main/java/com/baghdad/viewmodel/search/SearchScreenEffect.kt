package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.base.BaseUiEffect


sealed class SearchScreenEffect : BaseUiEffect {
    data object NavigateToLogin : SearchScreenEffect()

    data class NavigateToMovieDetails(val movieId: Long) : SearchScreenEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchScreenEffect()
    data class NavigateToActorDetails(val actorId: Long) : SearchScreenEffect()
}