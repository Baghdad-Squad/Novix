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
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import com.baghdad.repository.util.getPagedSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
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
        page: Int,
        pageSize: Int
    ): PagedResult<Actor> {
        return getPagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = ActorDto::toEntity,
            onStart = {
                deleteInvalidCacheOfMoreThanOneHour()
                if (page == 1) localRecentSearchDataSource.addRecentSearchQuery(name)
            },
            getCachedPage = { page, pageSize ->
                localActorDataSource.searchActorsByName(
                    name,
                    page,
                    pageSize
                )
            },
            getRemoteData = { page, _ ->
                searchRemoteDataSource.searchActors(query = name, page = page)
            },
            cacheData = {
                cacheActorSearchResult(
                    query = name,
                    actors = it
                )
            }
        )
    }

    private suspend fun cacheActorSearchResult(
        query: String,
        actors: List<ActorDto>
    ) {
        localActorDataSource.addActors(actors)
        localSearchQueryDataSource.addSearchQueries(actors.map { it.toSearchQueryDto(query) })
    }

    override suspend fun searchMoviesByTitle(
        title: String,
        page: Int,
        pageSize: Int
    ): PagedResult<Movie> {
        return getPagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = MovieDto::toEntity,
            onStart = {
                deleteInvalidCacheOfMoreThanOneHour()
                if (page == 1) localRecentSearchDataSource.addRecentSearchQuery(title)
            },
            getCachedPage = { page, pageSize ->
                localMovieDataSource.searchMoviesByTitle(
                    title,
                    page,
                    pageSize,
                )

            },
            getRemoteData = { page, _ ->
                updateGenreCache()
                val genres = localGenreDataSource.getMovieGenre(Locale.getDefault().language)
                searchRemoteDataSource.searchMovies(query = title, page = page, genres)
            },
            cacheData = {
                cacheMovieSearchResult(
                    query = title,
                    movies = it,
                )
            }
        )
    }

    private suspend fun cacheMovieSearchResult(
        query: String,
        movies: List<MovieDto>
    ) {
        localMovieDataSource.addMovies(movies)
        localSearchQueryDataSource.addSearchQueries(movies.map { it.toSearchQueryDto(query) })
    }

    override suspend fun searchTvShowsByName(
        title: String,
        page: Int,
        pageSize: Int
    ): PagedResult<TvShow> {
        return getPagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = TvShowDto::toEntity,
            onStart = {
                deleteInvalidCacheOfMoreThanOneHour()
                if (page == 1) localRecentSearchDataSource.addRecentSearchQuery(title)
            },
            getCachedPage = { page, pageSize ->
                localTvShowDataSource.searchTvShowsByTitle(
                    title,
                    page,
                    pageSize
                )
            },
            getRemoteData = { page, _ ->
                updateGenreCache()
                val genres = localGenreDataSource.getTvShowGenre(Locale.getDefault().language)
                searchRemoteDataSource.searchTvShows(query = title, page = page, genres)
            },
            cacheData = {
                cacheTvShowSearchResult(
                    query = title,
                    tvShows = it,
                )
            }
        )
    }

    private suspend fun cacheTvShowSearchResult(
        query: String,
        tvShows: List<TvShowDto>
    ) {
        localTvShowDataSource.addTvShows(tvShows)
        localSearchQueryDataSource.addSearchQueries(tvShows.map { it.toSearchQueryDto(query) })
    }


    private suspend fun updateGenreCache() {
        val lang = Locale.getDefault().language

        val movieGenres = remoteGenreDataSource.getMovieGenre(lang)
        val tvGenres = remoteGenreDataSource.getTvShowGenre(lang)

        movieGenres.forEach { localGenreDataSource.addGenre(it) }
        tvGenres.forEach { localGenreDataSource.addGenre(it) }
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
}


