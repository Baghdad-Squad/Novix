package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingTvShowScreenEffect : BaseUiEffect {
    data object NavigateBack : TrendingTvShowScreenEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : TrendingTvShowScreenEffect()
}