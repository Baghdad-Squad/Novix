package com.baghdad.viewmodel.search

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

data class SearchUiState(
    val searchText: String = "",
    val movies: List<Movie> = emptyList(),
    val tvShows: List<TvShow> = emptyList(),
    val actors: List<Actor> = emptyList(),
    val isLoading: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val selectedGenres: List<Genre> = emptyList(),
    val minimumYear: Int? = null,
    val maximumYear: Int? = null,
    val imdbRating: Int? = null,
    val isBottomSheetOpen: Boolean = false
)