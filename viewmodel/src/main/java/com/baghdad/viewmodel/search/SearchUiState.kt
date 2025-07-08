package com.baghdad.viewmodel.search

data class SearchUiState(
    val movies: List<String> = emptyList(),
    val actors: List<String> = emptyList(),
    val tvShows: List<String> = emptyList(),
    val selectedGenres: List<String> = emptyList(),
    val searchText: String = "",
    val minimumYear: Int? = null,
    val maximumYear: Int? = null,
    val imdbRating: Int? = null,
    val isLoading: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val isEmptyStateVisible: Boolean = false
)