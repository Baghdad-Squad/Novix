package com.baghdad.domain.usecase.gallery

import com.baghdad.domain.repository.ActorRepository

class GetGalleryImagesUseCase(
    private val actorRepository: ActorRepository
) {
    suspend fun invoke(actorId: Long): List<String>{
      return actorRepository.getActorGallery(actorId)
    }

}
