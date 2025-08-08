package com.baghdad.viewmodel.topRating

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow


fun Genre.toTopRatingGenreUiState() =
    TopRatingState.GenreUiState(
        id = this.id,
        name = this.name
    )

fun SavableMovie.toTopRatingMovieUiState() =
    TopRatingState.MovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
    )
fun TvShow.toTopRatingTvShowUiState() =
    TopRatingState.TvShowUiState(
        id = this.id,
        posterPictureURL = this.posterImageURL,
    )
