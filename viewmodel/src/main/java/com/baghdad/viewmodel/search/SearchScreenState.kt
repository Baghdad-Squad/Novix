package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.base.BaseUiState

data class SearchScreenState(
    val searchText: String = "",
    val movies: List<MovieUiState> = emptyList(),
    val tvShows: List<TvShowUiState> = emptyList(),
    val actors: List<ActorUiState> = emptyList(),
    val recentViewed: List<MediaUiState> = emptyList(),
    val recentSearch: List<RecentSearchUiState> = emptyList(),
    val bottomSheetUiState: FilterBottomSheetUiState
) : BaseUiState() {
    data class FilterBottomSheetUiState(
        val minimumYear: Int = 0,
        val maximumYear: Int = 0,
        val rate: Int = 0,
        val selectedGenres: List<GenreUiState> = emptyList(),
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

    data class MediaUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class RecentSearchUiState(
        val id: Long = 0,
        val query: String = ""
    )
}


