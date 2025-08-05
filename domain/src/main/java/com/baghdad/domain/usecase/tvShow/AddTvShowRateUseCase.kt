package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository

class AddTvShowRateUseCase(
    private val tvShowRepository: TvShowRepository,
) {
    suspend operator fun invoke(tvShowId: Long, rating: Int) {
        tvShowRepository.addTvShowRate(tvShowId, rating)
    }
}