package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.TvShow

class GetActorTvShowUseCase(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<TvShow> {
        return actorRepository.getActorTvShows(actorId)
    }
}