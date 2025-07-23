package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(): ContinueWatching {
    return ContinueWatching(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatching.ContentType.valueOf(contentType.name),
        userId = this.userId
    )
}

fun ContinueWatching.toDto(): ContinueWatchingDto{
    return ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType.name),
        userId = this.userId
    )
}