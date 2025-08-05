package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import javax.inject.Inject

class AddEpisodeRateUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        episodeRepository.addTvEpisodeRate(tvShowId, seasonNumber, episodeNumber, rating)
    }
}