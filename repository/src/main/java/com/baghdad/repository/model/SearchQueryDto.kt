package com.baghdad.repository.model

data class SearchQueryDto(
    val queryName: String,
    val mediaId: Long,
    val mediaType: MediaType
) {
    enum class MediaType {
        MOVIE,
        TV_SHOW,
        ACTOR
    }
}