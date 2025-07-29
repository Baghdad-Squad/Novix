package com.baghdad.viewmodel.movie

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingMoviesScreenState(
    val movies: Flow<PagingData<TrendingMovieUiState>> = flowOf(),
    val categories: List<TrendingCategoryUiState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedGenreId: Long? = null,
) : BaseUiState {

    data class TrendingMovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class TrendingCategoryUiState(
        val id: Long = 0,
        val name: String = "",
    )
}
