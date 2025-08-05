package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class GetActorTvShowUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<TvShow> {
        return actorRepository.getActorTvShows(actorId)
    }
}