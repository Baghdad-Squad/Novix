package com.baghdad.local_datasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recent Search")
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val recentSearchId: Long,
    val query: String,
    val time: String,
)
