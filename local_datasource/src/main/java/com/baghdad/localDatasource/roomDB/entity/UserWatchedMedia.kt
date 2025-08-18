package com.baghdad.localDatasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserWatchedMedia")
data class UserWatchedMedia(
    @PrimaryKey val contentId: Long,
    val genreIds: List<Long>,
    val contentImageUrl: String,
    val contentType: String,
    val userId: Long,
    val viewedAt: Long = System.currentTimeMillis(),
)


