package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import com.baghdad.repository.model.GenreDto

@Entity(
    tableName = "Genre",
    primaryKeys = ["id", "type"]
)
data class Genre(
    val id: Long,
    val name: String,
    val type: String
)

fun Genre.toDto(): GenreDto {
    return GenreDto(
        id = this.id,
        name = this.name,
        type = GenreDto.GenreType.valueOf(type)
    )
}