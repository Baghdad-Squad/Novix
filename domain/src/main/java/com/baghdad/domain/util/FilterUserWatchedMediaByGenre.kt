package com.baghdad.domain.util

import com.baghdad.domain.model.continueWatching.UserWatchedMedia

fun List<UserWatchedMedia>.filterByGenre(genreId: Long): List<UserWatchedMedia> {
    return this.filter { it.genreIds.contains(genreId) }
}