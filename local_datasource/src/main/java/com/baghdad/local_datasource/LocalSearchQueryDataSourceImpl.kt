package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.SearchQueryDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.model.SearchQueryDto
import com.baghdad.repository.model.SearchQueryDto.MediaType

class LocalSearchQueryDataSourceImpl(
    private val searchQueryDao: SearchQueryDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val actorDao: ActorDao,
) : LocalSearchQueryDataSource {
    override suspend fun addSearchQuery(searchQuery: SearchQueryDto) {
        executeWithErrorHandling {
            searchQueryDao.addSearchQuery(searchQuery.toLocalDto())
        }
    }

    override suspend fun getInvalidSearchQueries(
        timestamp: Long
    ): List<SearchQueryDto> {
        return executeWithErrorHandling {
            searchQueryDao.getInvalidSearchQueries(timestamp).map { it.toDto() }
        }
    }

    override suspend fun getAllSearchQueries(): List<SearchQueryDto> {
        return executeWithErrorHandling {
            searchQueryDao.getAllSearchQueries().map { it.toDto() }
        }
    }

    override suspend fun deleteInvalidSearchQueries(timestamp: Long) {
        executeWithErrorHandling {
            val inValidQueries = searchQueryDao.getInvalidSearchQueries(timestamp)
            inValidQueries.forEach {
                when (it.mediaType) {
                    MediaType.MOVIE.name -> {
                        movieDao.deleteMovieById(it.mediaId)
                    }

                    MediaType.TV_SHOW.name -> {
                        tvShowDao.deleteTvShowByID(it.mediaId)
                    }

                    MediaType.ACTOR.name -> {
                        actorDao.deleteActorById(it.mediaId)
                    }
                }
                searchQueryDao.deleteInvalidSearchQueries(timestamp)
            }
        }
    }

    override suspend fun deleteAllSearchQueries() {
        executeWithErrorHandling {
            searchQueryDao.deleteAllSearchQueries()
        }
    }
}