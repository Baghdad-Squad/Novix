package com.baghdad.viewmodel.categoryMovies

import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseUiState

data class CategoryMoviesState(
    override val isLoading: Boolean = false,
    val movies: List<MovieUiState> = emptyList<MovieUiState>(),
    val categoryName: String = ""
) : BaseUiState {
    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
}

fun Movie.toUiState(): CategoryMoviesState.MovieUiState {
    return CategoryMoviesState.MovieUiState(
        id = id,
        posterPictureURL = posterImageURL,
        isSaved = false
    )
}