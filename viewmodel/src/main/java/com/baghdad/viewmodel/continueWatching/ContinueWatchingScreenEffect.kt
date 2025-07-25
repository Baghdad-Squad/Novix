package com.baghdad.viewmodel.continueWatching

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface ContinueWatchingScreenEffect : BaseUiEffect{
    data object NavigateBack : ContinueWatchingScreenEffect
    data object NavigateToLogin : ContinueWatchingScreenEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : ContinueWatchingScreenEffect
    data class NavigateToMovieDetails(val movieId: Long) : ContinueWatchingScreenEffect
}