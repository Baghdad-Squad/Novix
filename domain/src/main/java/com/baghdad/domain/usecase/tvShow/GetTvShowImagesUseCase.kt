package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvShowImagesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long): List<String> {
        return tvShowRepository.getTvShowImages(tvId)
    }
}