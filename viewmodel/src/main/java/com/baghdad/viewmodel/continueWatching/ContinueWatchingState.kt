package com.baghdad.viewmodel.continueWatching

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ContinueWatchingState(
    val mediaFlow: Flow<PagingData<ContinueWatchingItemUiState>> = flowOf(),
    val genres: List<GenreUiState> = emptyList(),
    val selectedMovieGenreId: Long? = null,
    val selectedTvShowGenreId: Long? = null,
    val selectedMediaTabIsMovie: Boolean = true,
    val isUserLoggedIn: Boolean = false,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
    val isLoading: Boolean = false,
    ) : BaseUiState {
    data class ContinueWatchingItemUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
        val contentType: ContentType
    ) {
        enum class ContentType {
            MOVIE,
            TV_SHOW
        }
    }
    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )
}