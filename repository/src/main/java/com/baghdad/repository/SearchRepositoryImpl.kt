package com.baghdad.repository

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.datasource.local.RecentSearchDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toSavableMovie
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getFlowSafely
import com.baghdad.repository.util.getRemotePagedSafely
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: RemoteSearchDataSource,
    private val remoteGenreDataSource: RemoteGenreDataSource,
    private val recentSearchDataSource: RecentSearchDataSource,
    private val savableMovieDataSource: SavableMovieDataSource,
) : SearchRepository {

    override suspend fun searchActorsByName(
        name: String, page: Int, pageSize: Int
    ): PagedResult<Actor> = getRemotePagedSafely(
        page = page,
        pageSize = pageSize,
        mapToEntity = ActorDto::toEntity,
        onStart = {
            if (page == 1) recentSearchDataSource.addRecentSearchQuery(name)
        },
        getRemoteData = { page, _ ->
            searchRemoteDataSource.searchActors(query = name, page = page)
        })

    override suspend fun searchMoviesByTitle(
        title: String,
        page: Int,
        pageSize: Int,
    ): PagedResult<SavedMovie> {
        val savedMovies = savableMovieDataSource.getSavedMovies()
        return getRemotePagedSafely(
            page = page,
            pageSize = pageSize,
            mapToEntity = {
                it.toSavableMovie(
                    isSaved = savedMovies.containsKey(it.id),
                    listId = savedMovies[it.id],
                )
            },
            onStart = {
                if (page == 1)
                    recentSearchDataSource.addRecentSearchQuery(title)
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
                    recentSearchDataSource.addRecentSearchQuery(title)
            },
            getRemoteData = { page, _ ->
                val genres = remoteGenreDataSource.getTvShowGenre(Locale.getDefault().language)
                searchRemoteDataSource.searchTvShows(query = title, page = page, genres)
            })
    }

    override fun getRecentSearches(): Flow<List<RecentSearch>> {
        return getFlowSafely {
            recentSearchDataSource.getAllRecentSearches().map {
                it.map {
                    it.toEntity()
                }
            }
        }
    }

    override suspend fun deleteRecentSearchById(id: Long) {
        return executeSafely {
            recentSearchDataSource.deleteRecentSearchById(id)
        }
    }

    override suspend fun deleteAllRecentSearches() {
        return executeSafely {
            recentSearchDataSource.deleteAllRecentSearches()
        }
    }
}