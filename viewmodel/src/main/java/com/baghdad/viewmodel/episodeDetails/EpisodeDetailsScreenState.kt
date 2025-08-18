package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.BottomSheetType

data class EpisodeDetailsScreenState(
    val isEpisodeDetailsLoading: Boolean = false,
    val isEpisodeCastMembersLoading: Boolean = false,
    val episode: EpisodeUiState = EpisodeUiState(),
    val guestsOfHonor: List<GuestsOfHonerUiState> = emptyList(),
    val isOverviewExpanded: Boolean = false,
    val isRated: Boolean = true,
    val addToListBottomSheetState: AddToListBottomSheetState = AddToListBottomSheetState(),
    val rateEpisodeBottomSheetState: RateEpisodeBottomSheetState = RateEpisodeBottomSheetState(),
    val ratingStatus: RatingUiState = RatingUiState()
    ): BaseUiState {
    val isLoading: Boolean
        get() = isEpisodeDetailsLoading || isEpisodeCastMembersLoading

    data class EpisodeUiState(
        val id: Long = 0L,
        val title: String = "",
        val episodeNumber: Int = 0,
        val rating: Int = 0,
        val trailerUrl: String = "",
        val userRating: Int = 0,
        val duration: String = "",
        val releasedDate: String = "",
        val currentSeason: Int = 0,
        val overview: String = "",
        val categories: List<CategoryUiState> = emptyList(),
        val headerPictures: List<String> = emptyList(),
    )

    data class GuestsOfHonerUiState(
        val id: Long = 0L,
        val name: String = "",
        val characterName: String = "",
        val profilePictureURL: String = "",
    )

    data class CategoryUiState(
        val id: Long = 0L,
        val name: String = "",
    )

    data class AddToListBottomSheetState(
        val isVisible: Boolean = false,
    )

    data class RateEpisodeBottomSheetState(
        val isVisible: Boolean = false,
    )

    data class RatingUiState(
        val isBottomSheetVisible: Boolean = false,
        val bottomSheetType: BottomSheetType = BottomSheetType.Hidden,
    )

}
