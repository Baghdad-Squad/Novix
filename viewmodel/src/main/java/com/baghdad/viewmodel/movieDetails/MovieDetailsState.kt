package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiState

data class MovieDetailsState(
    val movieId: Long = 0L,
    val movieImages: List<String> = emptyList(),
    val movieName: String = "",
    val categories: List<String> = emptyList(),
    val rating: Double = 0.0,
    val duration: String = "",
    val date: String = "",
    val overView: String = "",
    val castes: List<ActorCardInfo> = emptyList(),
    val moreLikeThisMovie: List<MoreLikeThisMovie> = emptyList(),
    val isExtendText: Boolean = false,
    val isStared: Boolean = false,
    val isSaved: Boolean = false,
    val isHasTrailer: Boolean = true,
    override val isLoading: Boolean = false,
) : BaseUiState {

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
}