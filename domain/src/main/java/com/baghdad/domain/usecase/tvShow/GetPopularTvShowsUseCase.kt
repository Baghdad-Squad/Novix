package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class GetPopularTvShowsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke() = tvShowRepository.getPopularTvShows()
}