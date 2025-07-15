package com.baghdad.repository

import com.baghdad.domain.model.search.RecentSearch
import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.SearchQueryDto
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
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val localGenreDataSource: LocalGenreDataSource,
    private val localSearchQueryDataSource: LocalSearchQueryDataSource
) : SearchRepository {

    override suspend fun searchByName(query: String): SearchResult {
        return executeSafely {
            deleteInvalidCacheOfMoreThanOneHour()
            if (isUserSearchThisTermWithinHour(query)) {
                getLocalSearchResult(query)
            } else {
                updateGenreCache()
                val defaultLanguage = Locale.getDefault().language
                val searchResult = searchRemoteDataSource.searchMultiMedia(
                    query = query,
                    pageNumber = 1,
                    movieGenres = localGenreDataSource.getMovieGenre(defaultLanguage),
                    tvGenres = localGenreDataSource.getTvShowGenre(defaultLanguage)
                )
                cacheSearchResult(query, searchResult)
                searchResult.toEntity()
            }

        }
    }

    private suspend fun getLocalSearchResult(query: String): SearchResult {
        return SearchResultDto(
            actors = localActorDataSource.searchActorsByName(query),
            movies = localMovieDataSource.searchMoviesByTitle(query),
            tvShows = localTvShowDataSource.searchTvShowsByTitle(query)
        ).toEntity()
    }

    private suspend fun updateGenreCache() {
        val lang = Locale.getDefault().language

        val movieGenres = remoteGenreDataSource.getMovieGenre(lang)
        val tvGenres = remoteGenreDataSource.getTvShowGenre(lang)

        movieGenres.forEach { localGenreDataSource.addGenre(it) }
        tvGenres.forEach { localGenreDataSource.addGenre(it) }
    }

    private suspend fun cacheSearchResult(query: String, dto: SearchResultDto) {
        localRecentSearchDataSource.addRecentSearchQuery(query)

        dto.actors.forEach {
            localActorDataSource.addActor(it.name, it.imageUrl)
            localSearchQueryDataSource.addSearchQuery(
                SearchQueryDto(
                    queryName = query,
                    mediaId = it.id,
                    mediaType = SearchQueryDto.MediaType.ACTOR
                )
            )
        }
        dto.movies.forEach {
            localMovieDataSource.addMovie(it)
            localSearchQueryDataSource.addSearchQuery(
                SearchQueryDto(
                    queryName = query,
                    mediaId = it.id,
                    mediaType = SearchQueryDto.MediaType.MOVIE
                )
            )
        }
        dto.tvShows.forEach {
            localTvShowDataSource.addTvShow(it)
            localSearchQueryDataSource.addSearchQuery(
                SearchQueryDto(
                    queryName = query,
                    mediaId = it.id,
                    mediaType = SearchQueryDto.MediaType.TV_SHOW
                )
            )
        }
    }


    private suspend fun deleteInvalidCacheOfMoreThanOneHour() {
        val oneHourBeforeNow = System.currentTimeMillis() - 3600000
        localSearchQueryDataSource.deleteInvalidSearchQueries(oneHourBeforeNow)
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

