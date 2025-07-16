package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Episode

class GetTvShowEpisodesUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long): List<Episode> {
        return tvShowRepository.getTvShowEpisodes(tvShowId)
    }
}
