package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface CategoryTvShowsEffect : BaseUiEffect {
    object NavigateBack : CategoryTvShowsEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : CategoryTvShowsEffect
}