package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode

class GetTvShowSeasonEpisodesUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getTvShowSeasonEpisodes(tvShowId, seasonNumber)
    }
}
