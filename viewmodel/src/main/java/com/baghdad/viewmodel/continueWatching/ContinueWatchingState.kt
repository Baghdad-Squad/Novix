package com.baghdad.viewmodel.continueWatching

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ContinueWatchingState (
    val moviesFlow: Flow<PagingData<ContinueWatchingMovieUiState>> = flowOf(),
    val tvShowsFlow: Flow<PagingData<ContinueWatchingTvShowUiState>> = flowOf(),
    val genres: List<GenreUiState> = emptyList(),
    val selectedTab: Long = GenreUiState().id,
    override val isLoading: Boolean = false,

): BaseUiState{
    data class ContinueWatchingMovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
    data class ContinueWatchingTvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )
}