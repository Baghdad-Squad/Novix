package com.baghdad.repository.mapper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(
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

fun UserWatchedMedia.toDto() = ContinueWatchingDto(
    contentId = contentId,
    genreIds = genreIds,
    contentImageUrl = contentImageUrl,
    contentType = contentType.toDto(),
    userId = userId,
)

private fun ContinueWatchingDto.ContentType.toEntity() =
    UserWatchedMedia.ContentType.valueOf(name)

private fun UserWatchedMedia.ContentType.toDto() =
    ContinueWatchingDto.ContentType.valueOf(name)
