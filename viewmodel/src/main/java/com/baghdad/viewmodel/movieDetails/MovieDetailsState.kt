package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiState

data class MovieDetailsState(
    val isMovieGalleryLoading: Boolean = false,
    val isMovieDetailsLoading: Boolean = false,
    val isCastMemberLoading: Boolean = false,
    val isMoreLikeThisMovieLoading: Boolean = false,
    val movieId: Long = 0L,
    val movieImages: List<String> = emptyList(),
    val movieName: String = "",
    val movieTrailerURL: String = "",
    val categories: List<CategoryUiState> = emptyList(),
    val rating: Double = 0.0,
    val duration: Int = 0,
    val date: String = "",
    val overView: String = "",
    val castMembers: List<ActorCardInfo> = emptyList(),
    val posterImageURL: String = "",
    val moreLikeThisMovie: List<MoreLikeThisMovie> = emptyList(),
    val isExtendText: Boolean = false,
    val isStared: Boolean = false,
    val isSaved: Boolean = false,
    val isHasTrailer: Boolean = true,
) : BaseUiState {
    val isLoading: Boolean
        get() = isMovieDetailsLoading || isMovieGalleryLoading || isCastMemberLoading || isMoreLikeThisMovieLoading

    data class ActorCardInfo(
        val name: String = "",
        val imageUrl: String? = null,
        val characterName: String = "",
        val id: Int = 0,
    )

    data class MoreLikeThisMovie(
        val imageUrl: String = "",
        val id: Long = 0,
        val isSaved: Boolean = false,
    )

    data class CategoryUiState(
        val id: Long = 0,
        val name: String = "",
    )
}