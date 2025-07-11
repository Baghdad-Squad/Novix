package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "RecentViewed")
data class RecentViewed(
    @PrimaryKey val mediaId: Long,
    val mediaType: String,
    val timestamp: Long = System.currentTimeMillis()
)
