package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTvShowDataSourceImpl(
    private val tvShowDao: TvShowDao,
    private val genreDao: GenreDao
) : LocalTvShowDataSource {

    override suspend fun addTvShow(movie: TvShowDto) {
        return executeWithErrorHandling {
            val tvShowEntity = movie.toLocalDto()
            tvShowDao.upsertTvShow(tvShowEntity)
        }
    }

    override suspend fun addTvShows(tvShows: List<TvShowDto>) {
        executeWithErrorHandling {
            tvShowDao.upsertTvShows(tvShows.map(TvShowDto::toLocalDto))
        }
    }


    override suspend fun getTvShowById(id: Long): TvShowDto {
        return executeWithErrorHandling {
            val tvShow = tvShowDao.getTvShowById(id)
            val genres: List<GenreDto> = tvShow.genres.map {
                genreDao.getGenreById(it).toDto()
            }
            tvShow.toDto(genres)
        }
    }


    override suspend fun getAllTvShows(): Flow<List<TvShowDto>> {
        return executeWithErrorHandling {
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
        executeWithErrorHandling {
            tvShowDao.deleteTvShowByID(id)
        }
    }


    override suspend fun deleteAllTvShows() {
        executeWithErrorHandling {
            tvShowDao.deleteAll()
        }
    }


    override suspend fun updateTvShow(tvShow: TvShowDto) {
        executeWithErrorHandling {
            tvShowDao.upsertTvShow(tvShow.toLocalDto())
        }
    }

    override suspend fun searchTvShowsByTitle(
        title: String,
        page: Int,
        pageSize: Int
    ): List<TvShowDto> {
        return executeWithErrorHandling {
            val offset = (page - 1) * pageSize
            tvShowDao.getTvShowsFromSearchQuery(title, pageSize, offset).map {
                val genresDto = it.genres.map {
                    genreDao.getGenreById(it).toDto()
                }
                it.toDto(genresDto)
            }
        }
    }

    override suspend fun getTvShowCountByTitle(title: String): Int {
        return executeWithErrorHandling {
            tvShowDao.getTvShowCountBySearchQuery(title)
        }
    }
}

