package com.baghdad.domain.usecase.actorDetails

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor

class GetActorInfoUseCase(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): Actor {
        return actorRepository.getActorInfo(actorId)
    }
}