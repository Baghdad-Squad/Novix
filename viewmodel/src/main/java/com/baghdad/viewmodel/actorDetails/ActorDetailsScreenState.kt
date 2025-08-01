package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiState

data class ActorDetailsScreenState(
    val isActorInfoLoading: Boolean = false,
    val actorInfo: ActorInfoUiState = ActorInfoUiState(),
    val isGalleryLoading: Boolean = false,
    val gallery: List<String> = emptyList(),
    val isTopMoviePicksLoading: Boolean = false,
    val topMoviesPicks: List<MovieUiState> = emptyList(),
    val isTopTvShowPicksLoading: Boolean = false,
    val topTvShowsPicks: List<TvShowUiState> = emptyList(),
    val isTextExpanded: Boolean = false,
) : BaseUiState {
    val isLoading: Boolean
        get() = isActorInfoLoading || isGalleryLoading || isTopMoviePicksLoading || isTopTvShowPicksLoading

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
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