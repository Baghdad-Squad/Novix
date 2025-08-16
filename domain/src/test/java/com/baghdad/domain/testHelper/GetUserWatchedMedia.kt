package com.baghdad.domain.testHelper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia

fun getUserWatchedMedia(): UserWatchedMedia {
    return UserWatchedMedia(
        contentId = 123L,
        genreIds = listOf(1, 2, 3),
        contentImageUrl = "https://example.com/image.jpg",
        contentType = UserWatchedMedia.ContentType.MOVIE,
        isSaved = true,
        listId = 456L,
        userId = 1,
    )
}