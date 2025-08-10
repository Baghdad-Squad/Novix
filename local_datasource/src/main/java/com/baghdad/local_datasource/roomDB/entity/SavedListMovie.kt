package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_list_movies")
data class SavedListMovie(
    @PrimaryKey(autoGenerate = false) val movieId: Long,
    val listId: Long,
)
