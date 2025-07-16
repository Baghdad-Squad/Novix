package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow

class GetTvShowEpisodesUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long, seasonNumber: Int): List<TvShow> {
        return tvShowRepository.getTvShowEpisodes(tvId, seasonNumber)
    }
}