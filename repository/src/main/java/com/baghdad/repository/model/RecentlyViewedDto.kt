package com.baghdad.repository.model

data class RecentlyViewedDto(
    val contentId: Long,
    val contentImageUrl: String,
    val contentType: ContentType,
    val viewedAtEpochMillis: Long
) {
    enum class ContentType {
        MOVIE,
        TV_SHOW
    }
}
