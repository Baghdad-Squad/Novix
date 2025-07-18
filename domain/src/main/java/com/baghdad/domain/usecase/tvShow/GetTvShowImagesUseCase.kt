package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository

class GetTvShowImagesUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long): List<String> {
        return tvShowRepository.getTvShowImages(tvId)
    }
}