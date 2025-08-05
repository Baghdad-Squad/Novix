package com.baghdad.viewmodel.savedListDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface SavedListDetailsEffect : BaseUiEffect{
    data class NavigateToMovieDetails(val movieId: Long): SavedListDetailsEffect
    data class NavigateToTvShowDetails(val tvShowId: Long):SavedListDetailsEffect
    data object NavigateBack: SavedListDetailsEffect
}