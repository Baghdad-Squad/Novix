package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSearchDataSourceImpl(
    private val recentSearchDao: RecentSearchDao,
) : LocalRecentSearchDataSource {
    override suspend fun addRecentSearchQuery(query: String) =
        executeWithErrorHandling {
            val newRecentSearch = RecentSearch(
                query = query,
            )
            recentSearchDao.addRecentSearch(newRecentSearch)
        }

    override fun getAllRecentSearches(): Flow<List<RecentSearchDto>> =
        executeFlowWithErrorHandling {
            recentSearchDao.getAllRecentSearch().map { it ->
                it.sortedByDescending { it.searchedAt }.map { it.toDto() }
            }
        }

    override suspend fun deleteRecentSearchById(id: Long) =
        executeWithErrorHandling {
            recentSearchDao.deleteRecentSearchById(id)
        }

    override suspend fun deleteAllRecentSearches() =
        executeWithErrorHandling {
            recentSearchDao.clearAllRecentSearch()
        }
}