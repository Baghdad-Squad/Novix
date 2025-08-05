package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class AddTvShowRateUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) {
    suspend operator fun invoke(tvShowId: Long, rating: Int) {
        tvShowRepository.addTvShowRate(tvShowId, rating)
    }
}