package com.baghdad.viewmodel.continueWatching

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ContinueWatchingState(
    val mediaFlow: Flow<PagingData<ContinueWatchingMovieUiState>> = flowOf(),
    val genres: List<GenreUiState> = emptyList(),
    val selectedTab: Long? = null,
    val selectedMediaTabIsMovie: Boolean = true,
    override val isLoading: Boolean = false,

    ) : BaseUiState {
    data class ContinueWatchingMovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val contentType: ContentType
    ) {
        enum class ContentType {
            MOVIE,
            TV_SHOW
        }
    }

    data class ContinueWatchingTvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )


}