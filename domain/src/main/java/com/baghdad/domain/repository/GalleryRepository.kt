package com.baghdad.domain.repository

interface GalleryRepository {
    suspend fun getActorGalleryImages(): List<String>
}