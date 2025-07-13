package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseErrorState

data class SearchScreenState(
    val searchText: String = "",
    val selectedSearchTab: SearchTab = SearchTab.MOVIES,
    val movies: List<MovieUiState> = emptyList(),
    val tvShows: List<TvShowUiState> = emptyList(),
    val actors: List<ActorUiState> = emptyList(),
    val recentViewed: List<MediaUiState> = emptyList(),
    val recentSearch: List<RecentSearchUiState> = emptyList(),
    val bottomSheetUiState: FilterBottomSheetUiState = FilterBottomSheetUiState(),
    override val isLoading: Boolean = false,
    override val snackBarState: SnackBarState = SnackBarState(),
    override val baseErrorState: BaseErrorState? = null
) : BaseUiState {

    data class FilterBottomSheetUiState(
        val minimumYear: Int = 1990,
        val maximumYear: Int = 2025,
        val rate: Int = 0,
        val selectedGenres: List<GenreUiState> = emptyList(),
        val moviesGenres: List<GenreUiState> = emptyList(),
        val tvShowsGenres: List<GenreUiState> = emptyList(),
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

enum class SearchTab {
    MOVIES,
    TV_SHOWS,
    ACTORS,
}
