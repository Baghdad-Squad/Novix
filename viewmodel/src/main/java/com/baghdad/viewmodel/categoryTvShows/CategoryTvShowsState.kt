package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseUiState

data class CategoryTvShowsState(
    override val isLoading: Boolean = false,
    val tvShows: List<TvShowUiState> = emptyList<TvShowUiState>(),
    val categoryName: String = ""
) : BaseUiState {
    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
}

fun TvShow.toUiState(): CategoryTvShowsState.TvShowUiState {
    return CategoryTvShowsState.TvShowUiState(
        id = id,
        posterPictureURL = posterImageURL,
        isSaved = false
    )
}