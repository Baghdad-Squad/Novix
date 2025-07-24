package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.ContinueWatchingDto

@Entity(tableName = "ContinueWatching")
data class ContinueWatching(
    @PrimaryKey val contentId: Long,
    val genreIds: List<Long>,
    val contentImageUrl: String,
    val contentType: String,
    val userId: Long
)

fun ContinueWatching.toDto(): ContinueWatchingDto {
    return ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType),
        userId = this.userId
    )
}

fun ContinueWatchingDto.toLocalDto(): ContinueWatching {
    return ContinueWatching(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = contentType.name,
        userId = this.userId
    )
}