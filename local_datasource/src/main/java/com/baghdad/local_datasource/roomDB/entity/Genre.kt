package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.GenreDto

@Entity(tableName = "Genre")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    val type: String
)

fun Genre.toEntity(): GenreDto {
    return GenreDto(
        id = this.id,
        name = this.name
    )
}