package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.baghdad.local_datasource.roomDB.entity.FavoriteGenre

@Dao
interface FavoriteGenreDao {

    @Insert(onConflict = REPLACE)
    fun addFavoriteGenre(favoriteGenre: FavoriteGenre)

    @Query("SELECT * FROM favorite_genre ORDER BY count, timeStamp DESC ")
    fun getFavoriteGenres(): List<FavoriteGenre>

    @Query("SELECT * FROM favorite_genre WHERE genreId = :id")
    fun getFavoriteGenreById(id: Long): FavoriteGenre?

    @Transaction
    fun updateFavoriteGenreCount(id: Long, name: String) {
        val genre = getFavoriteGenreById(id)
        if (genre != null) {
            addFavoriteGenre(
                FavoriteGenre(
                    genreId = genre.genreId,
                    name = genre.name,
                    count = genre.count + 1
                )
            )
        } else {
            addFavoriteGenre(
                FavoriteGenre(
                    genreId = id,
                    name = name,
                    count = 1,
                )
            )
        }
    }
}