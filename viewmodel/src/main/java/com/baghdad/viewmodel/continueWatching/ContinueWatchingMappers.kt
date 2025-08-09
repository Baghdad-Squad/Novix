package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.entity.media.Genre

fun Genre.toContinueWatchingUiState() = ContinueWatchingState.GenreUiState(
    id = id,
    name = name,
)

fun ContinueWatching.toContinueWatchingUiState() = ContinueWatchingState.ContinueWatchingMovieUiState(
    id = contentId,
    posterPictureURL = contentImageUrl,
    isSaved = isSaved,
    savedListId = listId ?: -1L,
    contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.valueOf(contentType.name)
)