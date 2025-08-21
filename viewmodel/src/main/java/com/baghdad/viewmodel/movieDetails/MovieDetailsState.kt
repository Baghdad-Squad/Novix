package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import com.baghdad.viewmodel.shared.AddToListBottomSheetState
import com.baghdad.viewmodel.shared.BottomSheetType

data class MovieDetailsState(
    val isMovieGalleryLoading: Boolean = false,
    val isMovieDetailsLoading: Boolean = false,
    val isCastMemberLoading: Boolean = false,
    val isMoreLikeThisMovieLoading: Boolean = false,
    val movieImages: List<String> = emptyList(),
    val movieDetails: MovieDetails = MovieDetails(),
    val castMembers: List<ActorCardInfo> = emptyList(),
    val moreLikeThisMovie: List<MoreLikeThisMovie> = emptyList(),
    val isExtendText: Boolean = false,
    val userRating: Int = 0,
    val isRated: Boolean = true,
    val ratingStatus: RatingUiState = RatingUiState(),
    val isUserLoggedIn: Boolean = false,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
    ) : BaseUiState {
    val isLoading: Boolean
        get() = isMovieDetailsLoading || isMovieGalleryLoading || isCastMemberLoading || isMoreLikeThisMovieLoading


    data class MovieDetails(
        val movieName: String = "",
        val movieTrailerURL: String = "",
        val rating: Double = 0.0,
        val duration: Int = 0,
        val date: String = "",
        val overView: String = "",
        val posterImageURL: String = "",
        val isSaved: Boolean = false,
        val savedListId: Long = -1L,
        val isHasTrailer: Boolean = true,
        val categories: List<CategoryUiState> = emptyList(),
        val selectedMovieId: Long? = null,
    )

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
        val savedListId: Long = -1L,
    )

    data class CategoryUiState(
        val id: Long = 0,
        val name: String = "",
    )

    data class RatingUiState(
        val isBottomSheetVisible: Boolean = false,
        val bottomSheetType: BottomSheetType = BottomSheetType.Hidden,
    )
}