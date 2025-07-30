package com.baghdad.viewmodel.actorDetails

import com.baghdad.viewmodel.base.BaseUiState

data class ActorDetailsScreenState(
    val topMoviesPicks: List<MovieUiState> = emptyList(),
    val topTvShowsPicks: List<TvShowUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val actorInfo: ActorInfoUiState = ActorInfoUiState(),
    val isTextExpanded: Boolean = false,
    val isMoviesMoreThanTen: Boolean = false,
    val isTvShowsMoreThanTen: Boolean = false,
    val isGalleryMoreThanTen: Boolean = false,
    val isLoading: Boolean = false,
) : BaseUiState {
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