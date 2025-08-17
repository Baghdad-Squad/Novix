package com.baghdad.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.repository.datasource.local.RecentlyViewedDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentlyViewedRepositoryImpl
@Inject
constructor(
    val recentlyViewedDataSource: RecentlyViewedDataSource,
    private val savableMovieDataSource: SavableMovieDataSource,
) : RecentlyViewedRepository {
    override suspend fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>> {
        val savedMovies = savableMovieDataSource.getSavedMovies()
        return getFlowSafely {
            recentlyViewedDataSource.getAllRecentlyViewed().map {
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
            recentlyViewedDataSource.deleteAllRecentlyViewed()
        }
    }

    override suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed) {
        executeSafely {
            recentlyViewedDataSource.addMediaToRecentlyViewed(
                recentlyViewed.toDto(),
            )
        }
        recentlyViewedDataSource.addMediaToRecentlyViewed(recentlyViewed.toDto())
    }
}