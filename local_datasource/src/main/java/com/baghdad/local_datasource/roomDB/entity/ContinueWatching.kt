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
    val userId: Long,
    val viewedAt: Long = System.currentTimeMillis(),
)

fun ContinueWatching.toDto(): ContinueWatchingDto =
    ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType),
        userId = this.userId,
    )

fun List<ContinueWatching>.toDtos(): List<ContinueWatchingDto> = this.map(ContinueWatching::toDto)

fun ContinueWatchingDto.toLocalDto(): ContinueWatching =
    ContinueWatching(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = contentType.name,
        userId = this.userId,
    )
