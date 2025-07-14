package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.RecentSearchDao
import com.baghdad.local_datasource.database.dto.LocalRecentSearchDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.database.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSearchDataSourceImpl(
    private val recentSearchDao: RecentSearchDao,
) : LocalRecentSearchDataSource {
    override suspend fun addRecentSearchQuery(query: String) {
        executeWithErrorHandling {
            val newLocalRecentSearchDto = LocalRecentSearchDto(
                query = query,
            )
            recentSearchDao.addRecentSearch(newLocalRecentSearchDto)
        }
    }

    override fun getAllRecentSearches(): Flow<List<RecentSearchDto>> {
        return executeFlowWithErrorHandling {
            recentSearchDao.getAllRecentSearch().map { it ->
                it.sortedByDescending { it.searchedAt }.map { it.toDto() }
            }
        }
    }

    override suspend fun deleteRecentSearchById(id: Long) {
        executeWithErrorHandling {
            recentSearchDao.deleteRecentSearchById(id)
        }
    }

    override suspend fun deleteAllRecentSearches() {
        executeWithErrorHandling {
            recentSearchDao.clearAllRecentSearch()
        }
    }

}