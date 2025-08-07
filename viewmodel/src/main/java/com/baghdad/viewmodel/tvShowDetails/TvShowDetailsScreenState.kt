package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.BottomSheetType

data class TvShowDetailsScreenState(
    val tvShowInfo: TvShowInfoUiState = TvShowInfoUiState(),
    val castMembers: List<CastMemberUiState> = emptyList(),
    val episodes: List<EpisodeUiState> = emptyList(),
    val selectedSeasonIndex: Int = 0,
    val isBottomSheetVisible: Boolean = false,
    val isTextExpanded: Boolean = false,
    val hasTrailer: Boolean = true,
    val isRated: Boolean = true,
    val isCastMembersLoading: Boolean = false,
    val isEpisodesLoading: Boolean = false,
    val isTvShowDetailsLoading: Boolean = false,
    val ratingStatus: RatingUiState = RatingUiState(),
) : BaseUiState {
    val isLoading = isTvShowDetailsLoading || isCastMembersLoading
    data class TvShowInfoUiState(
        val title: String = "",
        val genres: List<GenreUiState> = emptyList(),
        val rating: Double = 0.0,
        val releaseDate: String = "",
        val seasonCount: Int = 0,
        val overView: String = "",
        val trailerURL: String = "",
        val posterPictureURL: String = "",
        val userRating: Int = 0,
        val headerImagesURLs: List<String> = emptyList()
    )

    data class GenreUiState(
        val id: Long?,
        val name: String = ""
    )

    data class CastMemberUiState(
        val id: Long?,
        val name: String = "",
        val imageUrl: String = "",
        val characterName: String = ""
    )

    data class EpisodeUiState(
        val id: Long?,
        val name: String = "",
        val episodeNumber: Int = 0,
        val rating: Double = 0.0,
        val duration: Int = 0,
        val releaseDate: String = "",
        val currentSeason: Int = 0,
    )

    data class RatingUiState(
        val isBottomSheetVisible: Boolean = false,
        val bottomSheetType: BottomSheetType = BottomSheetType.Hidden,
    )
}
