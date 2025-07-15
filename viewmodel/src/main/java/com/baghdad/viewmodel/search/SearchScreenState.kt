package com.baghdad.viewmodel.search

import com.baghdad.domain.model.search.RecentlyViewed.ContentType
import com.baghdad.domain.util.now
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.datetime.LocalDateTime

data class SearchScreenState(
    val searchText: String = "",
    val selectedSearchTab: SearchTab = SearchTab.MOVIES,
    val movies: List<MovieUiState> = emptyList(),
    val tvShows: List<TvShowUiState> = emptyList(),
    val actors: List<ActorUiState> = emptyList(),
    val recentViewed: List<RecentlyViewedUiState> = emptyList(),
    val recentSearch: List<RecentSearchUiState> = emptyList(),
    val bottomSheetUiState: FilterBottomSheetUiState = FilterBottomSheetUiState(),
    override val isLoading: Boolean = false,
) : BaseUiState {
    val searchFilter: SearchFilterUiState
        get() = if (selectedSearchTab == SearchTab.MOVIES) {
            bottomSheetUiState.moviesFilter
        } else {
            bottomSheetUiState.tvShowsFilter
        }
    data class FilterBottomSheetUiState(
        val moviesFilter: SearchFilterUiState = SearchFilterUiState(),
        val tvShowsFilter: SearchFilterUiState = SearchFilterUiState(),
        val isBottomSheetVisible: Boolean = false
    )

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class ActorUiState(
        val id: Long = 0,
        val name: String = "",
        val profilePictureURL: String = ""
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )

    data class RecentlyViewedUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val contentType: ContentType = ContentType.MOVIE,
        val isSaved: Boolean = false
    )

    data class RecentSearchUiState(
        val id: Long = 0,
        val query: String = ""
    )

    data class SearchFilterUiState(
        val minimumYear: Int = 1990,
        val maximumYear: Int = LocalDateTime.now().year,
        val minimumRating: Int = 0,
        val selectedGenres: List<GenreUiState> = emptyList(),
        val allGenres: List<GenreUiState> = emptyList(),
    )

    enum class SearchTab {
        MOVIES,
        TV_SHOWS,
        ACTORS,
    }
}


