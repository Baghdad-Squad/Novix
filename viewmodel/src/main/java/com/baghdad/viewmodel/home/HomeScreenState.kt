package com.baghdad.viewmodel.home

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenState(
    val isPopularLoading: Boolean = true,
    val popularItems: List<PopularItemUiState> = emptyList(),
    val isTopRatingLoading: Boolean = true,
    val topRatingItems: List<TopRatingItemUiState> = emptyList(),
    val isContinueWatchingLoading: Boolean = true,
    val continueWatchingItems: List<ContinueWatchingItemUiState> = emptyList(),
    val isUpcomingGenresLoading: Boolean = true,
    val upcomingGenres: List<GenreUiState> = emptyList(),
    val isUpcomingMoviesLoading: Boolean = true,
    val selectedUpcomingGenreId: Long? = null,
    val upcomingItems: Flow<PagingData<UpcomingItemUiState>> = flowOf(),
    val isLoading: Boolean = false,
) : BaseUiState {
    data class PopularItemUiState(
        val id: Long = 0L,
        val name: String = "",
        val rating: Double = 0.0,
        val imageUrl: String = "",
        val isSaved: Boolean = false,
        val type: Type = Type.MOVIE
    ) {
        enum class Type {
            MOVIE, TV_SHOW
        }
    }

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