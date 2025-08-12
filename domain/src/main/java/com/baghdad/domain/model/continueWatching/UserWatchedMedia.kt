package com.baghdad.domain.model.continueWatching

data class UserWatchedMedia(
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