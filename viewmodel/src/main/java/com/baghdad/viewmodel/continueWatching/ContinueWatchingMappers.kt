package com.baghdad.viewmodel.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie

fun Genre.toContinueWatchingUiState() = ContinueWatchingState.GenreUiState(
    id = id,
    name = name,
)

fun ContinueWatching.toContinueWatchingUiState() = ContinueWatchingState.ContinueWatchingMovieUiState(
    id = contentId,
    posterPictureURL = contentImageUrl,
    isSaved = true,
    contentType = ContinueWatchingState.ContinueWatchingMovieUiState.ContentType.valueOf(contentType.name)
)