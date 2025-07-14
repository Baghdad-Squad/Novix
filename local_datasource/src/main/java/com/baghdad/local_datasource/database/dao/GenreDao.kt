package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.baghdad.local_datasource.database.dto.LocalGenreDto

@Dao
interface GenreDao {
    @Insert
    fun addGenre(localGenreDto: LocalGenreDto)

    @Query("SELECT * FROM Genre")
    fun getAllGenres(): List<LocalGenreDto>

    @Query("SELECT * FROM Genre WHERE id = :id")
    fun getGenreById(id: Long): LocalGenreDto

    @Query("DELETE FROM Genre WHERE id = :id")
    fun deleteGenreById(id: Long)

    @Query("DELETE FROM Genre")
    fun deleteAllGenres()
}
