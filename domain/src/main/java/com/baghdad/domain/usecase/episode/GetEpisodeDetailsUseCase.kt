package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import javax.inject.Inject

class GetEpisodeDetailsUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int, episodeNumber: Int): Episode {
        return episodeRepository.getEpisodeDetails(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
    }
}