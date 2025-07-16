package com.baghdad.domain.usecase.tvShowDetails

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.person.Actor

class GetTvShowCastUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long): List<Actor> {
        return tvShowRepository.getTvShowCast(tvShowId)
    }
}