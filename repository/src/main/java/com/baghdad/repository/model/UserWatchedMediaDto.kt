package com.baghdad.repository.model

data class UserWatchedMediaDto(
    val contentId: Long,
    val genreIds: List<Long>,
    val contentImageUrl: String,
    val contentType: ContentType,
    val userId: Long
){
    enum class ContentType {
        MOVIE,
        TV_SHOW
    }
}