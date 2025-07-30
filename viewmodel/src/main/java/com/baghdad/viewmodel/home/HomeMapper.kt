package com.baghdad.viewmodel.home

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal

fun Genre.toUiState(): HomeScreenState.GenreUiState {
    return HomeScreenState.GenreUiState(
        id = id,
        name = name,
    )
}

fun ContinueWatching.toUiState(): HomeScreenState.ContinueWatchingItemUiState {
    return HomeScreenState.ContinueWatchingItemUiState(
        id = contentId,
        imageUrl = contentImageUrl,
        isSaved = false
    )
}

fun Movie.toPopularItemUiState(): HomeScreenState.PopularItemUiState {
    return HomeScreenState.PopularItemUiState(
        id = id,
        name = title,
        rating = averageRating.roundToFirstDecimal(),
        imageUrl = posterImageURL,
        isSaved = false,
        type = HomeScreenState.PopularItemUiState.Type.MOVIE
    )
}

fun TvShow.toPopularItemUiState(): HomeScreenState.PopularItemUiState {
    return HomeScreenState.PopularItemUiState(
        id = id,
        name = title,
        rating = averageRating.roundToFirstDecimal(),
        imageUrl = posterImageURL,
        isSaved = false,
        type = HomeScreenState.PopularItemUiState.Type.TV_SHOW
    )
}

fun Movie.toTopRatingItemUiState(): HomeScreenState.TopRatingItemUiState {
    return HomeScreenState.TopRatingItemUiState(
        id = id,
        imageUrl = posterImageURL,
        isSaved = false
    )
}

fun Movie.toUpcomingItemUiState(): HomeScreenState.UpcomingItemUiState {
    return HomeScreenState.UpcomingItemUiState(
        id = id,
        imageUrl = posterImageURL,
        isSaved = false
    )
}