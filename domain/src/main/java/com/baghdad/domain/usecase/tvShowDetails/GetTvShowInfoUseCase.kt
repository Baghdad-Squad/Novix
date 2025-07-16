package com.baghdad.domain.usecase.tvShowDetails

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow

class GetTvShowInfoUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long): TvShow {
        return tvShowRepository.getTvShowInfo(tvShowId)
    }
}