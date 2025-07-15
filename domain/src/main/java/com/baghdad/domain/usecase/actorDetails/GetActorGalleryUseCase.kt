package com.baghdad.domain.usecase.actorDetails

import com.baghdad.domain.repository.ActorRepository

class GetActorGalleryUseCase(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<String> {
        return actorRepository.getActorGallery(actorId)
    }
}