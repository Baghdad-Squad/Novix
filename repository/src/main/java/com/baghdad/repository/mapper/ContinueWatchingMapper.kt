package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto

fun ContinueWatchingDto.toEntity(): ContinueWatching =
    ContinueWatching(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatching.ContentType.valueOf(contentType.name),
        userId = this.userId,
    )

fun List<ContinueWatchingDto>.toEntities(): List<ContinueWatching> = this.map(ContinueWatchingDto::toEntity)

fun ContinueWatching.toDto(): ContinueWatchingDto =
    ContinueWatchingDto(
        contentId = this.contentId,
        genreIds = this.genreIds,
        contentImageUrl = this.contentImageUrl,
        contentType = ContinueWatchingDto.ContentType.valueOf(contentType.name),
        userId = this.userId,
    )
