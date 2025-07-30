package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.viewmodel.base.BaseUiState

data class TopMoviePicksState(
    val movies: List<MovieUiState> = emptyList(),
    val isLoading: Boolean = false,
) : BaseUiState{
    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )
}
