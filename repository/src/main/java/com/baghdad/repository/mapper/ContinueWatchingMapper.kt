package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

/**
 * remove this
 */
fun ContinueWatchingDto.toEntity(
    isSaved: Boolean,
    listId: Long?
): ContinueWatching =
    ContinueWatching(
        contentId = contentId,
        genreIds = genreIds,
        contentImageUrl = contentImageUrl,
        contentType = ContinueWatching.ContentType.valueOf(contentType.name),
        userId = userId,
        isSaved = isSaved,
        listId = listId,
    )

fun ContinueWatching.toDto(): ContinueWatchingDto =
    ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType.name),
        userId = this.userId,
    )
