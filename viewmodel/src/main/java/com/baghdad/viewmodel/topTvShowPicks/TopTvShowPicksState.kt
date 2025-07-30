package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.viewmodel.base.BaseUiState

data class TopTvShowPicksState(
    val tvShows: List<TvShowUiState> = emptyList(),
    val isLoading: Boolean = false,
) : BaseUiState{
    data class TvShowUiState(
        val id: Long,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
}
