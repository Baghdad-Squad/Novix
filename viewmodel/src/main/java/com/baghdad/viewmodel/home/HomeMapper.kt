package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.util.roundToFirstDecimal

fun Genre.toUiState(): HomeScreenState.GenreUiState =
    HomeScreenState.GenreUiState(
        id = id,
        name = name,
    )

fun ContinueWatching.toUiState(): HomeScreenState.ContinueWatchingItemUiState =
    HomeScreenState.ContinueWatchingItemUiState(
        id = contentId,
        imageUrl = contentImageUrl,
        isSaved = false,
        contentType = HomeScreenState.ContinueWatchingItemUiState.ContentType.valueOf(contentType.name),
    )

fun Movie.toPopularItemUiState(): HomeScreenState.PopularItemUiState =
    HomeScreenState.PopularItemUiState(
        id = id,
        name = title,
        rating = averageRating.roundToFirstDecimal(),
        imageUrl = posterImageURL,
        isSaved = false,
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

fun Movie.toTopRatingItemUiState(): HomeScreenState.TopRatingItemUiState =
    HomeScreenState.TopRatingItemUiState(
        id = id,
        imageUrl = posterImageURL,
        isSaved = false,
    )

fun Movie.toUpcomingItemUiState(): HomeScreenState.UpcomingItemUiState =
    HomeScreenState.UpcomingItemUiState(
        id = id,
        imageUrl = posterImageURL,
        isSaved = false,
    )
