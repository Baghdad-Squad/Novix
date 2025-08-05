package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRecentSearchDataSourceImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val logger: Logger,
) : LocalRecentSearchDataSource {
    override suspend fun addRecentSearchQuery(query: String) =
        executeWithErrorHandling(logger = logger) {
            val existingSearch = recentSearchDao.getRecentSearchByQuery(query)

            val recentSearch =
                existingSearch?.copy(searchedAt = System.currentTimeMillis())
                    ?: RecentSearch(query = query)

            recentSearchDao.upsertRecentSearch(recentSearch)
        }

    override fun getAllRecentSearches(): Flow<List<RecentSearchDto>> =
        executeFlowWithErrorHandling(logger = logger) {
            recentSearchDao.getAllRecentSearch().map { it ->
                it.sortedByDescending { it.searchedAt }.map { it.toDto() }
            }
        }

    override suspend fun deleteRecentSearchById(id: Long) =
        executeWithErrorHandling(logger = logger) {
            recentSearchDao.deleteRecentSearchById(id)
        }

    override suspend fun deleteAllRecentSearches() =
        executeWithErrorHandling(logger = logger) {
            recentSearchDao.clearAllRecentSearch()
        }
}
