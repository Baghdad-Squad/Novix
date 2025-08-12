package com.baghdad.localDatasource.roomDB.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recent_search",
    indices = [Index(value = ["query"], unique = true)],
)
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)

