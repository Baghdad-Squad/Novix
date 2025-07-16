package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.GenreDto

@Entity(tableName = "Genre")
data class Genre(
    @PrimaryKey
    var id: Long,
    var name: String,
    val type: String
)

fun Genre.toDto(): GenreDto {
    return GenreDto(
        id = this.id,
        name = this.name,
        type = GenreDto.GenreType.valueOf(type)
    )
}