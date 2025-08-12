package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(
    isSaved: Boolean,
    listId: Long?
) = ContinueWatching(
    contentId = contentId,
    genreIds = genreIds,
    contentImageUrl = contentImageUrl,
    contentType = contentType.toEntity(),
    userId = userId,
    isSaved = isSaved,
    listId = listId,
)

fun ContinueWatching.toDto() = ContinueWatchingDto(
    contentId = contentId,
    genreIds = genreIds,
    contentImageUrl = contentImageUrl,
    contentType = contentType.toDto(),
    userId = userId,
)

private fun ContinueWatchingDto.ContentType.toEntity() =
    ContinueWatching.ContentType.valueOf(name)

private fun ContinueWatching.ContentType.toDto() =
    ContinueWatchingDto.ContentType.valueOf(name)
