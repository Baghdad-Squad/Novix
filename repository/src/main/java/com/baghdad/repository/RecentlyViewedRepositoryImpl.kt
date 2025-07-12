package com.baghdad.repository

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.entity.media.Media
import com.baghdad.entity.media.Media.MediaType
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecentlyViewedRepositoryImpl(
    val localRecentlyViewedDataSource: LocalRecentlyViewedDataSource
) : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<Media>> {

        return flow {
            executeSafely {
                localRecentlyViewedDataSource.getAllRecentlyViewed().map { it.toEntity() }
            }
        }
    }

    override suspend fun deleteAllRecentlyViewed() {
        executeSafely {
            localRecentlyViewedDataSource.deleteAllRecentlyViewed()
        }
    }

    override suspend fun addMediaToRecentlyViewed(mediaId: Long, mediaType: MediaType) {
        executeSafely {
            localRecentlyViewedDataSource.addMediaToRecentlyViewed(
                mediaId,
                com.baghdad.repository.model.MediaType.valueOf(mediaType.name)
            )
        }
    }
}