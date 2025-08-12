package com.baghdad.localDatasource.roomDB.entity

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

fun ContinueWatching.toDto(): ContinueWatchingDto {
    return ContinueWatchingDto(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType),
        userId = userId,
    )
}


fun List<ContinueWatching>.toDtos(): List<ContinueWatchingDto> {
    return map(ContinueWatching::toDto)
}

fun ContinueWatchingDto.toLocalDto(): ContinueWatching {
    return ContinueWatching(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = contentType.name,
        userId = userId,
    )
}

