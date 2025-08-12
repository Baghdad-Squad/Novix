package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode
import javax.inject.Inject

class GetTvShowSeasonEpisodesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getTvShowSeasonEpisodes(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber
        )
    }
}