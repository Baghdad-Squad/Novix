package com.baghdad.domain.usecase.episode

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.TvShowRepository

class GetEpisodeAccountStatesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): MediaAccountStates {
        return episodeRepository.getEpisodeAccountStates(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
    }
}