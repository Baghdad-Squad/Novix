package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.database.dto.LocalGenreDto.Companion.GENRE_TABLE_NAME
import com.baghdad.repository.model.GenreDto

@Entity(tableName = GENRE_TABLE_NAME)
data class LocalGenreDto(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: Long = 0,
    @ColumnInfo(name = NAME)
    var name: String,
    @ColumnInfo(name = TYPE)
    val type: String
) {
    companion object {
        const val GENRE_TABLE_NAME = "Genre"
        const val ID = "id"
        const val NAME = "name"
        const val TYPE = "type"
    }
}

fun LocalGenreDto.toDto(): GenreDto {
    return GenreDto(
        id = this.id,
        name = this.name,
        type = GenreDto.GenreType.valueOf(type)
    )
}