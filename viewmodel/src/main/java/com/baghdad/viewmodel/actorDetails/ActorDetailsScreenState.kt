package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiState

data class ActorDetailsScreenState(
    val topMoviesPicks: List<MovieUiState> = emptyList(),
    val topTvShowsPicks: List<TvShowUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val actorInfo: ActorInfoUiState = ActorInfoUiState(),
    val isTextExpanded: Boolean = false,
    override val isLoading: Boolean = false,
) : BaseUiState {
    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = true
    )

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = true
    )

    data class ActorInfoUiState(
        val name: String = "",
        val headerPictures: List<String> = emptyList(),
        val birthdayDate: String = "",
        val placeOfBirth: String = "",
        val deathDate: String? = null,
        val biography: String = "",
        val department: String = ""
    )

}