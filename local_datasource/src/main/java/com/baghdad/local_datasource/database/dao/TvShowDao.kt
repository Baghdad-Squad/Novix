package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.dto.LocalTvShowDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Upsert
    suspend fun upsertTvShow(localTvShowDto: LocalTvShowDto)

    @Query("DELETE FROM TvShow WHERE id = :id")
    suspend fun deleteTvShowByID(id: Long)

    @Query("DELETE FROM TvShow")
    suspend fun deleteAll()

    @Query("SELECT * FROM TvShow WHERE id = :id")
    fun getTvShowById(id: Long): LocalTvShowDto

    @Query("SELECT * FROM TvShow")
    fun getAllTvShow(): Flow<List<LocalTvShowDto>>

    @Query("SELECT * FROM TvShow WHERE title LIKE '%' || :title || '%'")
    suspend fun searchTvShowsByTitle(title: String): List<LocalTvShowDto>

}