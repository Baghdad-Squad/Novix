package com.baghdad.viewmodel.categoryMovies

import com.baghdad.viewmodel.base.BaseUiState

data class CategoryMoviesState(
    override val isLoading: Boolean = false,
    val movies: List<MovieUiState> = emptyList<MovieUiState>(),
    val categoryName: String = ""
) : BaseUiState {
    class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
}