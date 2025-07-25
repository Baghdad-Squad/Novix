package com.baghdad.viewmodel.movie

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingMoviesEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long) : TrendingMoviesEffect()
    data object NavigateBack : TrendingMoviesEffect()
}