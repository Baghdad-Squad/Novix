package com.baghdad.repository

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.entity.media.Media
import com.baghdad.entity.media.Media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RecentlyViewedRepositoryImpl : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<Media>> {
        // TODO("Not yet implemented")
        return flowOf(emptyList())
    }

    override suspend fun deleteAllRecentlyViewed() {
        // TODO("Not yet implemented")
    }

    override suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: MediaType) {
        //TODO("Not yet implemented")
    }
}