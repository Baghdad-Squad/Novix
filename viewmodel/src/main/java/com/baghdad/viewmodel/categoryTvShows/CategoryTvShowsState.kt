package com.baghdad.viewmodel.categoryTvShows

import androidx.paging.PagingData
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoryTvShowsState(
    val isLoading: Boolean = false,
    val tvShowsFlow: Flow<PagingData<TvShowUiState>> = emptyFlow<PagingData<TvShowUiState>>(),
    val categoryName: String = ""
) : BaseUiState {
    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
    )
}

fun TvShow.toUiState(): CategoryTvShowsState.TvShowUiState {
    return CategoryTvShowsState.TvShowUiState(
        id = id,
        posterPictureURL = posterImageURL,
    )
}