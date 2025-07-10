package com.baghdad.local_datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.model.TvShowDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTvShowDataSourceImpl(
    private val tvShowDao: TvShowDao
) : LocalTvShowDataSource {
    @RequiresApi(Build.VERSION_CODES.O) // this because release date
    override suspend fun addTvShow(movie: TvShowDto) { // the same as the  override suspend fun updateTvShow
        val tvShowEntity = movie.toEntity()
        tvShowDao.upsertTvShow(tvShowEntity)
    }

    override suspend fun getTvShowById(id: Long): TvShowDto { //changed the getTvShowById to return one tv show not list
        return tvShowDao.getTvShowById(id).toDto()
    }

    override suspend fun getAllTvShows(): Flow<List<TvShowDto>> {
        return tvShowDao.getAllTvShow().map {
            it.map { it.toDto() }
        }
    }

    override suspend fun deleteTvShowById(id: Long) {
        return tvShowDao.deleteTvShowByID(id)
    }

    override suspend fun deleteAllTvShows() {
        return tvShowDao.deleteAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateTvShow(newMovie: TvShowDto) {
        val tvShowEntity = newMovie.toEntity()
        return tvShowDao.upsertTvShow(tvShowEntity)
    }
}