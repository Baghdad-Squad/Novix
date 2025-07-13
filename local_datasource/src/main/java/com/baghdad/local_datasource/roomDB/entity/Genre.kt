package com.baghdad.local_datasource.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.roomDB.entity.Genre.Companion.GERE_TABLE_NAME
import com.baghdad.repository.model.GenreDto

@Entity(tableName = GERE_TABLE_NAME)
data class Genre(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Long = 0,
    @ColumnInfo(name = NAME)
    var name: String,
    @ColumnInfo(name = TYPE)
    val type: String
) {
    companion object {
        const val GERE_TABLE_NAME = "Genre"
        const val ID = "id"
        const val NAME = "name"
        const val TYPE = "type"
    }
}

fun Genre.toDto(): GenreDto {
    return GenreDto(
        id = this.id,
        name = this.name,
        type = GenreDto.GenreType.valueOf(type)
    )
}