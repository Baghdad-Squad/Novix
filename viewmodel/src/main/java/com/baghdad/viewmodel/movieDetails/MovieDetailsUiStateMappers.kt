package com.baghdad.viewmodel.movieDetails

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie

fun Genre.toMoviesDetailsUiState() = MovieDetailsState.GenreUiState(
    id = id,
    name = name
)
fun Movie.toMovieDetailsUiState() = MovieDetailsState.MoreLikeThisMovie(
    id = id,
    imageUrl = posterImageURL
)