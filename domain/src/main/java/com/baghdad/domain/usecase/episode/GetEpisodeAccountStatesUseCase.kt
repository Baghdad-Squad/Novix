package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeAccountStatesUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Boolean {
        return episodeRepository.getEpisodeAccountStates(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
    }
}