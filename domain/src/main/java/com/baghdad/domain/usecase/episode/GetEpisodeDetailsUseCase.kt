package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode

class GetEpisodeDetailsUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(tvId: Long, seasonNumber: Int, episodeNumber: Int): Episode {
        return episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
    }
}