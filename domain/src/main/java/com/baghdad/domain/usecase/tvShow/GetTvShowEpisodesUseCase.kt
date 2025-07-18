package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.TvShow

class GetTvShowEpisodesUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getTvShowEpisodes(tvId, seasonNumber)
    }
}