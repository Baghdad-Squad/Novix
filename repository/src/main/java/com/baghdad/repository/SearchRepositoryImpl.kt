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
import com.baghdad.repository.model.SearchResultDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale

class SearchRepositoryImpl(
    private val searchRemoteDataSource: RemoteSearchDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localRecentSearchDataSource: LocalRecentSearchDataSource,
    private val localActorDataSource: LocalActorDataSource,
    private val localMovieDataSource: LocalMovieDataSource,
    private val localTvShowDataSource: LocalTvShowDataSource
) : SearchRepository {
    override suspend fun searchByName(query: String): SearchResult {

        return executeSafely {
            if (isUserSearchThisTermWithinHour(query)) {
                SearchResultDto(
                    actors = localActorDataSource.searchActorsByName(query),
                    movies = localMovieDataSource.searchMoviesByTitle(query),
                    tvShows = localTvShowDataSource.searchTvShowsByTitle(query)
                ).toEntity()
            } else {

                val movieGenres =
                    remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language)
                val tvShowsGenres =
                    remoteGenreDataSource.getTvShowGenre(language = Locale.getDefault().language)
                val searchResultDto = searchRemoteDataSource.searchMultiMedia(
                    query = query, pageNumber = 1, movieGenres, tvShowsGenres
                )

                localRecentSearchDataSource.addRecentSearchQuery(query)
                searchResultDto.actors.forEach {
                    localActorDataSource.addActor(it.name, it.imageUrl)
                }
                searchResultDto.movies.forEach {
                    localMovieDataSource.addMovie(it)
                }
                searchResultDto.tvShows.forEach {
                    localTvShowDataSource.addTvShow(it)
                }
                searchResultDto.toEntity()
            }
        }


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

    private suspend fun isUserSearchThisTermWithinHour(query: String): Boolean {
        val recentSearches = localRecentSearchDataSource.getAllRecentSearches().first()
        return recentSearches.any {
            it.query == query && (System.currentTimeMillis() - it.searchedAt) < 3600000
        }
    }
}

