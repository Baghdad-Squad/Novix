package com.baghdad.viewmodel.topRating

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie


fun Genre.toTopRatingGenreUiState() =
    TopRatingMovieState.GenreUiState(
        id = this.id,
        name = this.name
    )

fun Movie.toTopRatingMovieUiState() =
    TopRatingMovieState.MovieUiState(
        id = this.id,
        posterPictureURL = this.posterImageURL,
    )
