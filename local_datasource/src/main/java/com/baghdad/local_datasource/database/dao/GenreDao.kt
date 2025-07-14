package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.baghdad.local_datasource.database.entity.Genre

@Dao
interface GenreDao {
    @Insert
    fun addGenre(genre: Genre)

    @Query("SELECT * FROM Genre")
    fun getAllGenres(): List<Genre>

    @Query("SELECT * FROM Genre WHERE id = :id")
    fun getGenreById(id: Long): Genre

    @Query("DELETE FROM Genre WHERE id = :id")
    fun deleteGenreById(id: Long)

    @Query("DELETE FROM Genre")
    fun deleteAllGenres()
}
