package com.baghdad.viewmodel.home

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState

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
    val upcomingItems: List<UpcomingItemUiState> = emptyList(),
    val isUserLoggedIn: Boolean = false,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
    val isLoading: Boolean = false,
) : BaseUiState {
    data class PopularItemUiState(
        val id: Long = 0L,
        val name: String = "",
        val rating: Double = 0.0,
        val imageUrl: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
        val type: Type = Type.MOVIE
    ) {
        enum class Type {
            MOVIE, TV_SHOW
        }
    }

    data class TopRatingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
    )

    data class ContinueWatchingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
        val contentType: ContentType,
    ) {
        enum class ContentType {
            MOVIE,
            TV_SHOW,
        }
    }

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )

    data class UpcomingItemUiState(
        val id: Long = 0L,
        val imageUrl: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
    )
}