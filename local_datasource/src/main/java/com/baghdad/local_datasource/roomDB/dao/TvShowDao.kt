package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Upsert
    suspend fun upsertTvShow(tvShow: TvShow)

    @Query("DELETE FROM TvShow WHERE id = :id")
    suspend fun deleteTvShowByID(id: Long)

    @Query("DELETE FROM TvShow")
    suspend fun deleteAll()

    @Query("SELECT * FROM TVSHOW WHERE title = :title")
    fun getTvShowByTitle(title: String): Flow<List<TvShow>>

    @Query("SELECT * FROM TvShow")
    fun getAllTvShow(): Flow<List<TvShow>>

}