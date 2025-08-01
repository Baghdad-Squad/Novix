package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.FavoriteGenreDto

@Entity(tableName = "favorite_genre")
data class FavoriteGenre(
    @PrimaryKey val genreId: Long,
    val name: String,
    val count: Int = 0,
    val timeStamp: Long = System.currentTimeMillis()
)

fun FavoriteGenre.toDto(): FavoriteGenreDto {
    return FavoriteGenreDto(
        genreId = genreId,
        name = name,
        count = count,
        timeStamp = timeStamp
    )
}
