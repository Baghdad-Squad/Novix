package com.baghdad.viewmodel.topRating

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TopRatingState(
    val genres: List<GenreUiState> = emptyList(),
    val selectedGenreId: Long? = null,
    val moviesFlow: Flow<PagingData<MovieUiState>> = flowOf(),
    val tvShowsFlow: Flow<PagingData<TvShowUiState>> = flowOf(),
    val selectedTab: TopRatingTab = TopRatingTab.MOVIES,
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

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )


}
enum class TopRatingTab { MOVIES, TV_SHOWS }
