package com.baghdad.viewmodel.categoryMovies

import androidx.paging.PagingData
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class CategoryMoviesState(
    val isLoading: Boolean = false,
    val moviesFlow: Flow<PagingData<MovieUiState>> = flowOf(),
    val categoryName: String = "",
    val isUserLoggedIn: Boolean = false,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
) : BaseUiState {
    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L
    )
}

fun Movie.toUiState(): CategoryMoviesState.MovieUiState {
    return CategoryMoviesState.MovieUiState(
        id = id,
        posterPictureURL = posterImageURL,
        isSaved = false
    )
}