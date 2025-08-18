package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.UserWatchedMedia
import com.baghdad.repository.model.UserWatchedMediaDto

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
