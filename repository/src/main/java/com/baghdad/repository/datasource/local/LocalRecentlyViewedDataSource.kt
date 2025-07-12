package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.MediaDto
import com.baghdad.repository.model.MediaType

interface LocalRecentlyViewedDataSource {
    suspend fun getAllRecentlyViewed(): List<MediaDto>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: MediaType)

}