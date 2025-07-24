package com.baghdad.viewmodel.home

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface HomeScreenEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long) : HomeScreenEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : HomeScreenEffect
}