package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor
import javax.inject.Inject

class GetActorInfoUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): Actor {
        return actorRepository.getActorDetails(actorId = actorId)
    }
}