package com.baghdad.viewmodel.topRating

import com.baghdad.viewmodel.base.BaseUiState

data class TopRatingMovieState(
    val movies: List<MovieUiState> = emptyList(),
    val genres: List<GenreUiState> = emptyList(),
    val moviesByGenreFilter: MovieFilterUiState = MovieFilterUiState(),
    override val isLoading: Boolean = false,
) : BaseUiState{

    data class MovieFilterUiState(
        val moviesFilter: MovieFilter = MovieFilter(),
    )

    data class MovieFilter(
        val minimumYear: Int? = null,
        val maximumYear: Int? = null,
        val minimumRating: Int = 0,
        val selectedGenres: List<GenreUiState> = emptyList(),
        val allGenres: List<GenreUiState> = emptyList(),
    )

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )
}
