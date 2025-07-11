package com.baghdad.repository

import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.LocalSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.toEntity
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(
    val searchRemoteDataSource: RemoteSearchDataSource,
    val remoteGenreDataSource: RemoteGenreDataSource,
    val localSearchDataSource: LocalSearchDataSource
) : SearchRepository {
    override suspend fun searchByName(query: String): SearchResult {
        val searchResult = executeSafely {
            val movieGenres = remoteGenreDataSource.getMovieGenre(language = "en")
            val tvShowsGenres = remoteGenreDataSource.getTvShowGenre(language = "en")
            searchRemoteDataSource.searchMultiMedia(
                query = query, pageNumber = 1, movieGenres, tvShowsGenres
            ).toEntity()

        }
        executeSafely {
            localSearchDataSource.addRecentSearchQuery(query)
        }
        return searchResult

    }

    override fun getRecentSearches(): Flow<List<RecentSearch>> {
        return getFlowSafely {
            localSearchDataSource.getAllRecentSearches().map {
                it.map {
                    it.toEntity()
                }
            }
        }
    }


    override suspend fun deleteRecentSearchById(id: Long) {
        return executeSafely {
            localSearchDataSource.deleteRecentSearchById(id)
        }
    }

    override suspend fun deleteAllRecentSearches() {
        return executeSafely {
            localSearchDataSource.deleteAllRecentSearches()
        }
    }

    private suspend fun isUserSearchThisTermBeforeWithinHour(query: String): Boolean {
        val recentSearches = localSearchDataSource.getAllRecentSearches().first()
        return recentSearches.any {
            it.query == query && (System.currentTimeMillis() - it.searchedAt) < 3600000
        }
    }
}

