package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository

class GetPopularTvShowsUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke() = tvShowRepository.getPopularTvShows()
}