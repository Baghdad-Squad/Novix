package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentlyViewedRepositoryImpl(
    val localRecentlyViewedDataSource: LocalRecentlyViewedDataSource
) : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>> {
        return getFlowSafely {
            localRecentlyViewedDataSource.getAllRecentlyViewed()
                .map { it.map(RecentlyViewedDto::toEntity) }
        }
    }

    override suspend fun deleteAllRecentlyViewed() {
        executeSafely {
            localRecentlyViewedDataSource.deleteAllRecentlyViewed()
        }
    }

    override suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed) {
        executeSafely {
            localRecentlyViewedDataSource.addMediaToRecentlyViewed(
                recentlyViewed.toDto()
            )
        }
    }
}