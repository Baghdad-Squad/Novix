package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.person.CastMember

class GetTvShowCastMembersUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long): List<CastMember> {
        return tvShowRepository.getTvShowCastMembers(tvId)
    }
}