package com.baghdad.repository.mapper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.repository.model.UserWatchedMediaDto

fun UserWatchedMediaDto.toEntity(
    isSaved: Boolean,
    listId: Long?
) = UserWatchedMedia(
    contentId = contentId,
    genreIds = genreIds,
    contentImageUrl = contentImageUrl,
    contentType = contentType.toEntity(),
    userId = userId,
    isSaved = isSaved,
    listId = listId,
)

fun UserWatchedMedia.toDto(): UserWatchedMediaDto =
    UserWatchedMediaDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = contentType.toDto(),
        userId = this.userId,
    )

private fun UserWatchedMediaDto.ContentType.toEntity() =
    UserWatchedMedia.ContentType.valueOf(name)

private fun UserWatchedMedia.ContentType.toDto() =
    UserWatchedMediaDto.ContentType.valueOf(name)
