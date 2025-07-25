package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow


fun Genre.toUiState() =
    TrendingTvShowScreenState.GenreUiState(
        id = this.id,
        name = this.name
    )

fun TvShow.toUiState() =
    TrendingTvShowScreenState.TvShowUiState(
        id = this.id,
        posterPictureURL = this.posterImageURL,
    )