package com.baghdad.viewmodel.movie

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingMoviesEffect : BaseUiEffect {
    data class NavigateToDetails(val movieId: Long) : TrendingMoviesEffect()
    object NavigateBack : TrendingMoviesEffect()
}