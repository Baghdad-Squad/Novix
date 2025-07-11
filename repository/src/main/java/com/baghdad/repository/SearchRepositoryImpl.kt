package com.baghdad.repository

import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
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
    private val searchRemoteDataSource: RemoteSearchDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localRecentSearchDataSource: LocalRecentSearchDataSource,
    private val localActorDataSource: LocalActorDataSource,
    private val localMovieDataSource: LocalMovieDataSource,
    private val localTvShowDataSource: LocalTvShowDataSource
) : SearchRepository {
    override suspend fun searchByName(query: String): SearchResult {
        val searchResult = executeSafely {
            val movieGenres = remoteGenreDataSource.getMovieGenre(language = "en")
            val tvShowsGenres = remoteGenreDataSource.getTvShowGenre(language = "en")
            searchRemoteDataSource.searchMultiMedia(
                query = query, pageNumber = 1, movieGenres, tvShowsGenres
            )
        }

        executeSafely {
            localRecentSearchDataSource.addRecentSearchQuery(query)
            searchResult.actors.forEach {
                localActorDataSource.addActor(it.name, it.imageUrl)
            }
            searchResult.movies.forEach {
                localMovieDataSource.addMovie(it)
            }
            searchResult.tvShows.forEach {
                localTvShowDataSource.addTvShow(it)
            }
        }

        return searchResult.toEntity()

    }

    override fun getRecentSearches(): Flow<List<RecentSearch>> {
        return getFlowSafely {
            localRecentSearchDataSource.getAllRecentSearches().map {
                it.map {
                    it.toEntity()
                }
            }
        }
    }


    override suspend fun deleteRecentSearchById(id: Long) {
        return executeSafely {
            localRecentSearchDataSource.deleteRecentSearchById(id)
        }
    }

    override suspend fun deleteAllRecentSearches() {
        return executeSafely {
            localRecentSearchDataSource.deleteAllRecentSearches()
        }
    }

    private suspend fun isUserSearchThisTermBeforeWithinHour(query: String): Boolean {
        val recentSearches = localRecentSearchDataSource.getAllRecentSearches().first()
        return recentSearches.any {
            it.query == query && (System.currentTimeMillis() - it.searchedAt) < 3600000
        }
    }
}

