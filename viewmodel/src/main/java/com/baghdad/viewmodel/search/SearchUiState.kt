package com.baghdad.viewmodel.search

data class SearchUiState(
    val searchText: String = "",
    val movies: List<String> = emptyList(),
    val tvShows: List<String> = emptyList(),
    val actors: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val selectedGenres: List<Genres> = emptyList(),
    val minimumYear: Int? = null,
    val maximumYear: Int? = null,
    val imdbRating: Int? = null,
    val isBottomSheetOpen: Boolean = false
)

data class Genres(val id: Int = 0, val name: String)