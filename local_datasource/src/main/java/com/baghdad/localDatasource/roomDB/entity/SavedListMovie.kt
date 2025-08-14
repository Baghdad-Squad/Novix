package com.baghdad.localDatasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_list_movies")
data class SavedListMovie(
    @PrimaryKey val movieId: Long,
    val listId: Long,
)
