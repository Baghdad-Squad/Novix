package com.baghdad.viewmodel.trendingTvShow

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingTvShowScreenState(
    val trendingTvShows: Flow<PagingData<TvShowUiState>> = flowOf(),
    val genres: List<GenreUiState> = emptyList(),
    val selectedGenreId: Long? = null,
    override val isLoading: Boolean = false
) : BaseUiState {

    data class GenreUiState(
        val id: Long,
        val name: String
    )

    data class TvShowUiState(
        val id: Long,
        val posterPictureURL: String,
        val isSaved: Boolean = false
    )
}