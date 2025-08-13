package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class DeleteTvShowRateUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) {
    suspend operator fun invoke(tvShowId: Long) {
        tvShowRepository.deleteTvShowRate(tvShowId = tvShowId)
    }
}