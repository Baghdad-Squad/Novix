package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository

class AddEpisodeRateUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        seriesId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        episodeRepository.addTvEpisodeRate(seriesId, seasonNumber, episodeNumber, rating)
    }
}