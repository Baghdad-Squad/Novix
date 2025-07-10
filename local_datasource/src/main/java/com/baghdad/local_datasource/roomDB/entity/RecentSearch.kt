package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecentSearch")
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
