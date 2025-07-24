package com.baghdad.viewmodel.topRating

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TopRatingMovieState(
    val genres: List<GenreUiState> = emptyList(),
    val selectedGenreId: Long = 0L,
    val moviesFlow: Flow<PagingData<MovieUiState>> = flowOf(),
    override val isLoading: Boolean = false
) : BaseUiState {

    data class GenreUiState(
        val id: Long= 0L,
        val name: String= ""
    )

    data class MovieUiState(
        val id: Long,
        val posterPictureURL: String,
        val isSaved: Boolean = false
    )

}