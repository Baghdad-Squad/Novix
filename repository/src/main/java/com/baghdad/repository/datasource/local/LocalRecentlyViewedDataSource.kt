package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.MediaDto

interface LocalRecentlyViewedDataSource {
    suspend fun getAllRecentlyViewed(): List<MediaDto>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: String)

}