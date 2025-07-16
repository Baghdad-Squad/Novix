package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.person.CastMember

class GetTvShowCastUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long): List<CastMember> {
        return tvShowRepository.getTvShowCastMembers(tvShowId)
    }
}