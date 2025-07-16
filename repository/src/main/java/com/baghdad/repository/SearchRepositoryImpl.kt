package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toSearchQueryDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.actor.ActorDto
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

    override suspend fun searchActorsByName(
        name: String,
        page: Int
    ): PagedResult<Actor> {
        return executeSafely {
            deleteInvalidCacheOfMoreThanOneHour()
            if (isUserSearchThisTermWithinHour(name)) {
                PagedResult(
                    data = localActorDataSource.searchActorsByName(name).map(ActorDto::toEntity),
                    nextKey = null,
                    prevKey = null
                )
            } else {
                updateGenreCache()
                val response = searchRemoteDataSource.searchActors(name, page)
                cacheActorSearchResult(name, response.data)

                PagedResult(
                    data = response.data.map(ActorDto::toEntity),
                    nextKey = response.nextKey,
                    prevKey = response.prevKey
                )
            }
        }
    }

    override suspend fun searchMoviesByTitle(
        title: String,
        page: Int
    ): PagedResult<Movie> {
        return executeSafely {
            deleteInvalidCacheOfMoreThanOneHour()

            val shouldFetchFromRemote = !isUserSearchThisTermWithinHour(title) ||
                    !hasEnoughCachedDataForPage(title, page)

            var hasNextPage = false
            if (shouldFetchFromRemote) {
                updateGenreCache()
                val genres = localGenreDataSource.getMovieGenre(Locale.getDefault().language)
                val response = searchRemoteDataSource.searchMovies(title, page, genres)
                hasNextPage = response.nextKey != null
                cacheMovieSearchResult(isFirstSearch = page == 1, title, response.data)
            }
            val localMovies = localMovieDataSource.searchMoviesByTitle(title, page, 20)
            val totalCount = localMovieDataSource.getMovieCountByTitle(title)
            if (!hasNextPage) {
                hasNextPage = (page * 20) < totalCount
            }
            PagedResult(
                data = localMovies.map(MovieDto::toEntity),
                nextKey = if (hasNextPage) page + 1 else null,
                prevKey = if (page > 1) page - 1 else null
            )
        }
    }

    private suspend fun hasEnoughCachedDataForPage(title: String, page: Int): Boolean {
        val requiredItemsCount = page * 20
        val cachedCount = localMovieDataSource.getMovieCountByTitle(title)
        return cachedCount >= requiredItemsCount
    }

    override suspend fun searchTvShowsByName(
        title: String,
        page: Int
    ): PagedResult<TvShow> {
        return executeSafely {
            deleteInvalidCacheOfMoreThanOneHour()
            if (isUserSearchThisTermWithinHour(title)) {
                PagedResult(
                    data = localTvShowDataSource.searchTvShowsByTitle(title)
                        .map(TvShowDto::toEntity),
                    nextKey = null,
                    prevKey = null
                )
            } else {
                updateGenreCache()
                val genres = localGenreDataSource.getTvShowGenre(Locale.getDefault().language)
                val response = searchRemoteDataSource.searchTvShows(title, page, genres)
                cacheTvShowsSearchResult(title, response.data)

                PagedResult(
                    data = response.data.map(TvShowDto::toEntity),
                    nextKey = response.nextKey,
                    prevKey = response.prevKey
                )
            }

        }
    }

    private suspend fun updateGenreCache() {
        val lang = Locale.getDefault().language

        val movieGenres = remoteGenreDataSource.getMovieGenre(lang)
        val tvGenres = remoteGenreDataSource.getTvShowGenre(lang)

        movieGenres.forEach { localGenreDataSource.addGenre(it) }
        tvGenres.forEach { localGenreDataSource.addGenre(it) }
    }

    private suspend fun cacheActorSearchResult(query: String, actors: List<ActorDto>) {
        localRecentSearchDataSource.addRecentSearchQuery(query)
        localActorDataSource.addActors(actors)
        localSearchQueryDataSource.addSearchQueries(actors.map { it.toSearchQueryDto(query) })
    }

    private suspend fun cacheMovieSearchResult(
        isFirstSearch: Boolean,
        query: String,
        movies: List<MovieDto>
    ) {
        if (isFirstSearch) localRecentSearchDataSource.addRecentSearchQuery(query)
        localMovieDataSource.addMovies(movies)
        localSearchQueryDataSource.addSearchQueries(movies.map { it.toSearchQueryDto(query) })
    }

    private suspend fun cacheTvShowsSearchResult(query: String, tvShows: List<TvShowDto>) {
        localRecentSearchDataSource.addRecentSearchQuery(query)
        localTvShowDataSource.addTvShows(tvShows)
        localSearchQueryDataSource.addSearchQueries(tvShows.map { it.toSearchQueryDto(query) })
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

