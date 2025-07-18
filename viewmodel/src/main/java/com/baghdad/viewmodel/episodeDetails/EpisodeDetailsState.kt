package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseUiState

data class EpisodeDetailsState(
    val episodeId: Long = 0L,
    val episodeImages: List<String> = emptyList(),
    val episodeName: String = "",
    val categories: List<String> = emptyList(),
    val rating: Double = 0.0,
    val duration: String = "",
    val numberOfSequence : String = "",
    val overView: String = "",
    val guestsOfHonor: List<GuestsOfHoner> = emptyList(),
    val isExtendText: Boolean = false,
    val isStared: Boolean = false,
    val isSaved: Boolean = false,
    val isHasTrailer: Boolean = true,
    override val isLoading: Boolean = false,
) : BaseUiState {
    data class GuestsOfHoner(
        val name: String = "",
        val imageUrl: String? = null,
        val characterName: String = "",
        val id: Int = 0,
    )
}

