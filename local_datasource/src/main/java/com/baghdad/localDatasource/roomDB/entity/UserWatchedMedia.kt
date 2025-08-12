package com.baghdad.localDatasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.UserWatchedMediaDto

@Entity(tableName = "UserWatchedMedia")
data class UserWatchedMedia(
    @PrimaryKey val contentId: Long,
    val genreIds: List<Long>,
    val contentImageUrl: String,
    val contentType: String,
    val userId: Long,
    val viewedAt: Long = System.currentTimeMillis(),
)

fun UserWatchedMedia.toDto(): UserWatchedMediaDto {
    return UserWatchedMediaDto(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = UserWatchedMediaDto.ContentType.valueOf(contentType),
        userId = userId,
    )
}


fun List<UserWatchedMedia>.toDtos(): List<UserWatchedMediaDto> {
    return map(UserWatchedMedia::toDto)
}

fun UserWatchedMediaDto.toLocalDto(): UserWatchedMedia {
    return UserWatchedMedia(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = contentType.name,
        userId = userId,
    )
}

