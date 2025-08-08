package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState

data class TopMoviePicksState(
    val movies: List<MovieUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState()
) : BaseUiState {
    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L
    )
}
