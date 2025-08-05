package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.TvShowRepository

class GetTvShowAccountStatesUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long, ): MediaAccountStates {
        return tvShowRepository.getTvShowAccountStates(tvShowId = tvShowId,)
    }
}