package com.baghdad.domain.model


data class ContinueWatching(
    val contentId: Long,
    val genreIds: List<Long>,
    val contentImageUrl: String,
    val isSaved: Boolean,
    val listId: Long?,
    val contentType: ContentType,
    val userId: Long
){
    enum class ContentType {
        MOVIE,
        TV_SHOW
    }
}