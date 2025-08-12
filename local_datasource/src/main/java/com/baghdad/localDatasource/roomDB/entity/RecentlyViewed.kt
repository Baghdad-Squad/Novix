package com.baghdad.localDatasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recently_viewed")
data class RecentlyViewed(
    @PrimaryKey val contentId: Long,
    val contentType: String,
    val contentImageURL: String,
    val viewedAt: Long = System.currentTimeMillis()
)
