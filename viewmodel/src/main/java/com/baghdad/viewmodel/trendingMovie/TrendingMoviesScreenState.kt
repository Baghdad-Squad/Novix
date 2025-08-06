package com.baghdad.viewmodel.trendingMovie

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingMoviesScreenState(
    val movies: Flow<PagingData<TrendingMovieUiState>> = flowOf(),
    val categories: List<TrendingCategoryUiState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedGenreId: Long? = null,
    val isUserLoggedIn: Boolean = false,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
) : BaseUiState {

    data class TrendingMovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L
    )

    data class TrendingCategoryUiState(
        val id: Long = 0,
        val name: String = "",
    )
}
