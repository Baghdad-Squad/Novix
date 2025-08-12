package com.baghdad.repository.mapper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(
    isSaved: Boolean,
    listId: Long?,
): UserWatchedMedia =
    UserWatchedMedia(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = UserWatchedMedia.ContentType.valueOf(contentType.name),
        userId = this.userId,
        isSaved = isSaved,
        listId = listId,
    )

fun UserWatchedMedia.toDto(): ContinueWatchingDto =
    ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType.name),
        userId = this.userId,
    )
