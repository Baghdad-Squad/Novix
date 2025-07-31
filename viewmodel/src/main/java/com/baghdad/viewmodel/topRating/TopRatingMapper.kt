package com.baghdad.viewmodel.topRating

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow


fun Genre.toTopRatingGenreUiState() =
    TopRatingState.GenreUiState(
        id   = this.id,
        name = this.name
    )

fun Movie.toTopRatingMovieUiState() =
    TopRatingState.MovieUiState(
        id = this.id,
        posterPictureURL = this.posterImageURL,
    )
fun TvShow.toTopRatingTvShowUiState() =
    TopRatingState.TvShowUiState(
        id = this.id,
        posterPictureURL = this.posterImageURL,
    )
