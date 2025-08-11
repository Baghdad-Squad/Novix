package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentlyViewedRepositoryImpl @Inject constructor(
    private val localRecentlyViewedDataSource: LocalRecentlyViewedDataSource,
    private val savableMovieDataSource: LocalSavableMovieDataSource,
) : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>> {
        val savedMovies = savableMovieDataSource.getSavedMovies()
        return getFlowSafely {
            localRecentlyViewedDataSource.getAllRecentlyViewed().map {
                it.map { dto ->
                    dto.toEntity(
                        isSaved = savedMovies.containsKey(dto.contentId),
                        listId = savedMovies[dto.contentId],
                    )
                }
            }
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
                recentlyViewedDto = recentlyViewed.toDto(),
            )
        }
    }
}