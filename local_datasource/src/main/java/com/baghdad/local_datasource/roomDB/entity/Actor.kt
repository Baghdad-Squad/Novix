package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Actor")
data class Actor(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val profilePictureURL: String,
)
