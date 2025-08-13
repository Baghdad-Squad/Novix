package com.baghdad.localDatasource

import com.baghdad.localDatasource.errorHandler.executeFlowWithErrorHandling
import com.baghdad.localDatasource.errorHandler.executeWithErrorHandling
import com.baghdad.localDatasource.mapper.toDto
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.entity.RecentSearch
import com.baghdad.repository.datasource.local.RecentSearchDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentSearchDataSourceImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val logger: Logger,
) : RecentSearchDataSource {
    override suspend fun addRecentSearchQuery(query: String) {
        executeWithErrorHandling(logger = logger) {
            val existingSearch = recentSearchDao.getRecentSearchByQuery(query)

            val recentSearch =
                existingSearch?.copy(searchedAt = System.currentTimeMillis())
                    ?: RecentSearch(query = query)

            recentSearchDao.upsertRecentSearch(recentSearch)
        }
    }

    override fun getAllRecentSearches(): Flow<List<RecentSearchDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            recentSearchDao.getAllRecentSearch().map { it ->
                it.sortedByDescending { it.searchedAt }.map { it.toDto() }
            }
        }
    }

    override suspend fun deleteRecentSearchById(id: Long) {
        executeWithErrorHandling(logger = logger) {
            recentSearchDao.deleteRecentSearchById(id)
        }
    }

    override suspend fun deleteAllRecentSearches() {
        executeWithErrorHandling(logger = logger) {
            recentSearchDao.clearAllRecentSearch()
        }
    }

}
