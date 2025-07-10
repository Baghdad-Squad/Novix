package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovie(movie: Movie)

    @Query("DELEtE FROM Movie WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

    @Query("DELETE FROM Movie")
    suspend fun deleteAll()

    @Query("SELECT * FROM Movie WHERE title LIKE '%' || :title || '%' COLLATE NOCASE")
    fun getMovieByTitle(title: String): Flow<List<Movie>>

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flow<List<Movie>>


}