package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTvShowDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val genreDao: GenreDao,
    private val logger: Logger
) : LocalTvShowDataSource {

    override suspend fun addTvShow(movie: TvShowDto) {
        return executeWithErrorHandling(logger = logger) {
            val tvShowEntity = movie.toLocalDto()
            tvShowDao.upsertTvShow(tvShowEntity)
        }
    }

    override suspend fun addTvShows(tvShows: List<TvShowDto>) {
        executeWithErrorHandling(logger = logger) {
            tvShowDao.upsertTvShows(tvShows.map(TvShowDto::toLocalDto))
        }
    }


    override suspend fun getTvShowById(id: Long): TvShowDto {
        return executeWithErrorHandling(logger = logger) {
            val tvShow = tvShowDao.getTvShowById(id)
            val genres: List<GenreDto> = tvShow.genres.map {
                genreDao.getGenreById(it).toDto()
            }
            tvShow.toDto(genres)
        }
    }


    override suspend fun getAllTvShows(): Flow<List<TvShowDto>> {
        return executeWithErrorHandling(logger = logger) {
            tvShowDao.getAllTvShow().map {
                it.map {
                    val genres = it.genres.map {
                        genreDao.getGenreById(it).toDto()
                    }
                    it.toDto(genres)
                }
            }
        }
    }


    override suspend fun deleteTvShowById(id: Long) {
        executeWithErrorHandling(logger = logger) {
            tvShowDao.deleteTvShowByID(id)
        }
    }


    override suspend fun deleteAllTvShows() {
        executeWithErrorHandling(logger = logger) {
            tvShowDao.deleteAll()
        }
    }


    override suspend fun updateTvShow(tvShow: TvShowDto) {
        executeWithErrorHandling(logger = logger) {
            tvShowDao.upsertTvShow(tvShow.toLocalDto())
        }
    }

    override suspend fun searchTvShowsByTitle(
        title: String,
        page: Int,
        pageSize: Int
    ): List<TvShowDto> {
        return executeWithErrorHandling(logger = logger) {
            val pageOffset = calculatePageOffset(pageSize, page)
            val tvShows = tvShowDao.getTvShowsFromSearchQuery(title, pageSize, pageOffset)
            val genresMap = getGenresMap(tvShows)
            tvShows.toDtos(genresMap)
        }
    }

    private suspend fun getGenresMap(
        tvShows: List<TvShow>
    ): Map<Long, Genre> {
        return executeWithErrorHandling(logger = logger) {
            val allGenreIds = tvShows.flatMap { it.genres }.distinct()
            genreDao.getGenresByIds(allGenreIds).associateBy { it.id }
        }
    }
}

