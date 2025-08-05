package com.baghdad.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import com.baghdad.repository.util.getRemotePagedSafely
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: RemoteSearchDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val localRecentSearchDataSource: LocalRecentSearchDataSource
) : SearchRepository {

    override suspend fun searchActorsByName(
        name: String, page: Int, pageSize: Int
    ): PagedResult<Actor> = getRemotePagedSafely(
        page = page,
        pageSize = pageSize,
        mapToEntity = ActorDto::toEntity,
        onStart = {
            if (page == 1) localRecentSearchDataSource.addRecentSearchQuery(name)
        },
        getRemoteData = { page, _ ->
            searchRemoteDataSource.searchActors(query = name, page = page)
        })

    override suspend fun searchMoviesByTitle(
        title: String,
        page: Int,
        pageSize: Int
    ): PagedResult<Movie> {
        return getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = MovieDto::toEntity,
            onStart = {
                if (page == 1)
                    localRecentSearchDataSource.addRecentSearchQuery(title)
            },
            getRemoteData = { page, _ ->
                val genres = remoteGenreDataSource.getMovieGenre(Locale.getDefault().language)
                searchRemoteDataSource.searchMovies(query = title, page = page, genres)
            })
    }

    override suspend fun searchTvShowsByName(
        title: String, page: Int, pageSize: Int
    ): PagedResult<TvShow> {
        return getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = TvShowDto::toEntity,
            onStart = {
                if (page == 1)
                    localRecentSearchDataSource.addRecentSearchQuery(title)
            },
            getRemoteData = { page, _ ->
                val genres = remoteGenreDataSource.getTvShowGenre(Locale.getDefault().language)
                searchRemoteDataSource.searchTvShows(query = title, page = page, genres)
            })
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


