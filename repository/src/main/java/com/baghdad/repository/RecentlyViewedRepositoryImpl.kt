package com.baghdad.repository

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.entity.media.Media

class RecentlyViewedRepositoryImpl : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): List<Media> {
        // TODO("Not yet implemented")
        return emptyList()
    }

    override suspend fun deleteAllRecentlyViewed() {
        // TODO("Not yet implemented")
    }

    override suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: String) {
        //TODO("Not yet implemented")
    }
}