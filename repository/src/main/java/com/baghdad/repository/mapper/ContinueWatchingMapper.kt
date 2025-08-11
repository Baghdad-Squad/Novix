package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(
    isSaved: Boolean,
    listId: Long?
): ContinueWatching {
    return ContinueWatching(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = ContinueWatching.ContentType.valueOf(contentType.name),
        userId = userId,
        isSaved = isSaved,
        listId = listId,
    )
}

fun ContinueWatching.toDto(): ContinueWatchingDto {
    return ContinueWatchingDto(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType.name),
        userId = userId,
    )
}
