package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.entity.media.Genre

fun Genre.toContinueWatchingUiState() = ContinueWatchingState.GenreUiState(
    id = id,
    name = name,
)

fun UserWatchedMedia.toContinueWatchingUiState() = ContinueWatchingState.ContinueWatchingMovieUiState(
    id = contentId,
    posterPictureURL = contentImageUrl,
    isSaved = isSaved,
    savedListId = listId ?: -1L,
    contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.valueOf(contentType.name)
)