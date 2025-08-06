package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface TopMoviePicksEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long): TopMoviePicksEffect
    data object NavigateBack: TopMoviePicksEffect
    data object NavigateToLogin : TopMoviePicksEffect
}
