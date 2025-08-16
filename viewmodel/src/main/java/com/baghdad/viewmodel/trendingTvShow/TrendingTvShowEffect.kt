package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingTvShowEffect : BaseUiEffect {
    data object NavigateBack : TrendingTvShowEffect()
    data class NavigateToTvShowDetails(val tvShowId: Long) : TrendingTvShowEffect()
}