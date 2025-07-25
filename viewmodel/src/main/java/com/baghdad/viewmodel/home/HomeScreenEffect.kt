package com.baghdad.viewmodel.home

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface HomeScreenEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long) : HomeScreenEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : HomeScreenEffect
    data object NavigateToContinueWatching : HomeScreenEffect
    data object NavigateToTopRating : HomeScreenEffect
    data object NavigateToMovies : HomeScreenEffect
    data object NavigateToTvShows : HomeScreenEffect
    data object NavigateToActors : HomeScreenEffect
    data object NavigateToLogin : HomeScreenEffect
}