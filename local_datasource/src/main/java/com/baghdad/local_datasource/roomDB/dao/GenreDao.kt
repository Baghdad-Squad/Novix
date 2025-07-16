package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.baghdad.local_datasource.roomDB.entity.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = REPLACE)
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
