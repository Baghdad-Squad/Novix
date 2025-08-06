package com.baghdad.domain.model

data class RatedMedia(
    val id: Long,
    val userRating: Double?,
    val posterImageURL: String,
    val contentType: ContentType
) {
    enum class ContentType {
        MOVIE,
        TV_SHOW
    }
}