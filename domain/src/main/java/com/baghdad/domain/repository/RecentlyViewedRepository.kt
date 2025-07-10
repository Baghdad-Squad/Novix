package com.baghdad.domain.repository

import com.baghdad.entity.media.Media

interface RecentlyViewedRepository {
    suspend fun getAllRecentlyViewed() : List<Media>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addMediaToRecentlyViewed(mediaId:Long, mediaType:String)
}