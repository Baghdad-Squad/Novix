package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.TvShowRepository

import javax.inject.Inject

class GetTvShowAccountStatesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long): MediaAccountStates {
        return tvShowRepository.getTvShowAccountStates(tvShowId = tvShowId)
    }
}