package com.baghdad.domain.repository

import com.baghdad.entity.media.Media
import com.baghdad.entity.media.Media.MediaType
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    suspend fun getAllRecentlyViewed(): Flow<List<Media>>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: MediaType)
}