package com.baghdad.repository.mapper

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun RecentlyViewedDto.toEntity() = RecentlyViewed(
    contentId = contentId,
    contentImageUrl = contentImageUrl,
    contentType = RecentlyViewed.ContentType.valueOf(contentType.name),
    viewedAt = Instant.fromEpochMilliseconds(viewedAtEpochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
)

@OptIn(ExperimentalTime::class)
fun RecentlyViewed.toDto() = RecentlyViewedDto(
    contentId = contentId,
    contentImageUrl = contentImageUrl,
    contentType = RecentlyViewedDto.ContentType.valueOf(contentType.name),
    viewedAtEpochMillis = viewedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
)