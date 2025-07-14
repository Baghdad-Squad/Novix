package com.baghdad.domain.usecase.gallery

import com.baghdad.domain.repository.GalleryRepository

class GetGalleryImagesUseCase(
    private val galleryRepository: GalleryRepository
) {
    suspend fun getActorImages(): List<String>{
      return galleryRepository.getActorGalleryImages()
    }

}
