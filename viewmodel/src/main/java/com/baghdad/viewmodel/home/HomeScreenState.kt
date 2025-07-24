package com.baghdad.viewmodel.home

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenState(
    val isPopularLoading: Boolean = false,
    val popularItems: List<PopularItemUiState> = emptyList(),
    val isTopRatingLoading: Boolean = false,
    val topRatingItems: List<TopRatingItemUiState> = emptyList(),
    val isContinueWatchingLoading: Boolean = false,
    val continueWatchingItems: List<ContinueWatchingItemUiState> = emptyList(),
    val isUpcomingGenresLoading: Boolean = false,
    val upcomingGenres: List<GenreUiState> = emptyList(),
    val selectedUpcomingGenreId: Long = 0L,
    val upcomingItems: Flow<PagingData<UpcomingItemUiState>> = flowOf(),
    override val isLoading: Boolean = false,
) : BaseUiState {
    data class PopularItemUiState(
        val id: Long = 0L,
        val name: String = "",
        val rating: Double = 0.0,
        val imageUrl: String = "",
        val isSaved: Boolean = false
    )

    data class TopRatingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false
    )

    data class ContinueWatchingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )

    data class UpcomingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false
    )
}