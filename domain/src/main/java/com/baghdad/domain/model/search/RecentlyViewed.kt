package com.baghdad.domain.model.search

import kotlinx.datetime.LocalDateTime

data class RecentlyViewed(
    val contentId: Long,
    val contentImageUrl: String,
    val contentType: ContentType,
    val viewedAt: LocalDateTime
) {
    enum class ContentType {
        MOVIE,
        TV_SHOW
    }
}
