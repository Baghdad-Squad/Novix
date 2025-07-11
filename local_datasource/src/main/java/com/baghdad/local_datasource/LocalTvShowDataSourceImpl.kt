package com.baghdad.local_datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.model.TvShowDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTvShowDataSourceImpl(
    private val tvShowDao: TvShowDao
) : LocalTvShowDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addTvShow(movie: TvShowDto) =
        executeWithErrorHandling {
            val tvShowEntity = movie.toEntity()
            tvShowDao.upsertTvShow(tvShowEntity)
        }

    override suspend fun getTvShowById(id: Long): TvShowDto =
        executeWithErrorHandling {
            tvShowDao.getTvShowById(id).toDto()
        }

    override suspend fun getAllTvShows(): Flow<List<TvShowDto>> =
        executeWithErrorHandling {
            tvShowDao.getAllTvShow().map {
                it.map { it.toDto() }
            }
        }

    override suspend fun deleteTvShowById(id: Long) =
        executeWithErrorHandling {
            tvShowDao.deleteTvShowByID(id)
        }

    override suspend fun deleteAllTvShows() =
        executeWithErrorHandling {
            tvShowDao.deleteAll()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateTvShow(newMovie: TvShowDto) =
        executeWithErrorHandling {
            val tvShowEntity = newMovie.toEntity()
            tvShowDao.upsertTvShow(tvShowEntity)
        }
}