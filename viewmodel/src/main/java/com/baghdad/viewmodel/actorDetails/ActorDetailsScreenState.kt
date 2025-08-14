package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState

data class ActorDetailsScreenState(
    val isActorInfoLoading: Boolean = false,
    val actorInfo: ActorInfoUiState = ActorInfoUiState(),
    val isGalleryLoading: Boolean = false,
    val gallery: List<String> = emptyList(),
    val selectedImage: String = "",
    val isTopMoviePicksLoading: Boolean = false,
    val topMoviesPicks: List<MovieUiState> = emptyList(),
    val isTopTvShowPicksLoading: Boolean = false,
    val topTvShowsPicks: List<TvShowUiState> = emptyList(),
    val isTextExpanded: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val selectedMovieId: Long? = null,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
) : BaseUiState {
    val isLoading: Boolean
        get() = isActorInfoLoading || isGalleryLoading || isTopMoviePicksLoading || isTopTvShowPicksLoading

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L
    )

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
    )

    data class ActorInfoUiState(
        val name: String = "",
        val headerPictures: List<String> = emptyList(),
        val birthdayDate: String? = null,
        val placeOfBirth: String = "",
        val deathDate: String? = null,
        val biography: String = "",
        val department: String = ""
    )

}