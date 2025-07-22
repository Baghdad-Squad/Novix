package com.baghdad.viewmodel.movie

import com.baghdad.viewmodel.base.BaseUiState

data class MovieScreenState(
    val movies: List<MovieUiState> = emptyList(),
    val categories: List<CategoryUiState> = emptyList(),
    override val isLoading: Boolean = false
) : BaseUiState {

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class CategoryUiState(
        val id: Long = 0,
        val name: String = "",
        val isSelected: Boolean = false
    )
}