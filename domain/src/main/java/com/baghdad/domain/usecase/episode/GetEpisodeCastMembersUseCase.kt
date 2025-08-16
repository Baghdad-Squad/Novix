package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.person.CastMember
import javax.inject.Inject

class GetEpisodeCastMembersUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember> {
        return episodeRepository.getEpisodeCastMembers(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
    }
}