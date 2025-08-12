package com.baghdad.viewmodel.home

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.util.roundToFirstDecimal

fun Genre.toUiState(): HomeScreenState.GenreUiState =
    HomeScreenState.GenreUiState(
        id = id,
        name = name,
    )

fun UserWatchedMedia.toUiState(): HomeScreenState.ContinueWatchingItemUiState =
    HomeScreenState.ContinueWatchingItemUiState(
        id = contentId,
        imageUrl = contentImageUrl,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
        contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.valueOf(contentType.name),
    )

fun SavedMovie.toPopularItemUiState(): HomeScreenState.PopularItemUiState =
    HomeScreenState.PopularItemUiState(
        id = movie.id,
        name = movie.title,
        rating = movie.averageRating.roundToFirstDecimal(),
        imageUrl = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
        type = HomeScreenState.PopularItemUiState.Type.MOVIE,
    )

fun TvShow.toPopularItemUiState(): HomeScreenState.PopularItemUiState =
    HomeScreenState.PopularItemUiState(
        id = id,
        name = title,
        rating = averageRating.roundToFirstDecimal(),
        imageUrl = posterImageURL,
        isSaved = false,
        type = HomeScreenState.PopularItemUiState.Type.TV_SHOW,
    )

fun SavedMovie.toTopRatingItemUiState(): HomeScreenState.TopRatingItemUiState =
    HomeScreenState.TopRatingItemUiState(
        id = movie.id,
        imageUrl = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
    )

fun SavedMovie.toUpcomingItemUiState(): HomeScreenState.UpcomingItemUiState =
    HomeScreenState.UpcomingItemUiState(
        id = this.movie.id,
        imageUrl = this.movie.posterImageURL,
        isSaved = this.isSaved,
        savedListId = this.listId ?: -1L,
    )
