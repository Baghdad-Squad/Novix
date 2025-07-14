package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovie(movie: Movie)

    @Query("DELETE FROM Movie WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

    @Query("DELETE FROM Movie")
    suspend fun deleteAll()

    @Query("SELECT * FROM Movie WHERE id =  :id")
    suspend fun getMovieById(id: Long): Movie

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM Movie WHERE title LIKE '%' || :title || '%'")
    suspend fun searchMoviesByTitle(title: String): List<Movie>
}