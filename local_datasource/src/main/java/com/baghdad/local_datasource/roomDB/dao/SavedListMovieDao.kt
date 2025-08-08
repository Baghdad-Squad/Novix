package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baghdad.local_datasource.roomDB.entity.SavedListMovie

@Dao
interface SavedListMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies: List<SavedListMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: SavedListMovie)

    @Query("DELETE FROM saved_list_movies WHERE listId = :listId")
    suspend fun deleteAllByListId(listId: Long)

    @Query("DELETE FROM saved_list_movies WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: Long)

    @Query("SELECT * FROM saved_list_movies WHERE movieId = :movieId LIMIT 1")
    suspend fun getSavedMovieById(movieId: Long): SavedListMovie?

    @Query("SELECT * FROM saved_list_movies")
    suspend fun getSavedMovies(): List<SavedListMovie>
}
