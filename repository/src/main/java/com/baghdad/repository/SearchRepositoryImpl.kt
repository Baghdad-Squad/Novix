package com.baghdad.repository

import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchRepositoryImpl(
    val searchRemoteDataSource: RemoteSearchDataSource,
    val remoteSearchDataSource: RemoteGenreDataSource
) : SearchRepository {
    override suspend fun searchByName(query: String): SearchResult {
        val movieGenres = remoteSearchDataSource.getMovieGenre(language = "en")
        val tvShowsGenres = remoteSearchDataSource.getTvShowGenre(language = "en")
        return searchRemoteDataSource.searchMultiMedia(
            query = query,
            pageNumber = 1,
            movieGenres,
            tvShowsGenres
        ).toEntity()

    }

    override suspend fun getRecentSearches(): Flow<List<RecentSearch>> {
        // TODO("Not yet implemented")
        return flowOf(emptyList())
    }

    override suspend fun deleteRecentSearchById(id: Long) {
        // TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecentSearches() {
        // TODO("Not yet implemented")
    }
}