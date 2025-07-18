package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface TopTvShowPicksEffect : BaseUiEffect {
    data class NavigateToTvShowDetails(val tvShowId: Long): TopTvShowPicksEffect
    data object NavigateBack: TopTvShowPicksEffect
}
