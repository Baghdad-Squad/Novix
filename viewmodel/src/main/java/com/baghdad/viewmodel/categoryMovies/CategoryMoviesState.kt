package com.baghdad.viewmodel.categoryMovies

import androidx.paging.PagingData
import com.baghdad.domain.model.savedList.SavableMovie
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

fun SavableMovie.toUiState(): CategoryMoviesState.MovieUiState =
    CategoryMoviesState.MovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
    )
