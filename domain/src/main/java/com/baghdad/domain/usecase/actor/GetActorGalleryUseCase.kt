package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorGalleryUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<String> {
        return actorRepository.getActorGallery(actorId = actorId)
    }
}