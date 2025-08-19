package com.baghdad.viewmodel.movieDetails

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.util.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMYYYYFormat

fun SavedMovie.toMoreLikeThisMovie() = MovieDetailsState.MoreLikeThisMovie(
    imageUrl = movie.posterImageURL,
    id = movie.id,
    isSaved = isSaved,
    savedListId = listId ?: -1L
)

fun CastMember.toActorCardInfo(): MovieDetailsState.ActorCardInfo = MovieDetailsState.ActorCardInfo(
    name = actor.name,
    imageUrl = actor.profilePictureURL,
    characterName = characterName,
    id = actor.id.toInt()
)

fun SavedMovie.toMovieDetailsStateUpdate(): MovieDetailsState.() -> MovieDetailsState = {
    copy(
        movieName = movie.title,
        movieTrailerURL = movie.trailerURL,
        overView = movie.overview,
        rating = movie.averageRating.roundToFirstDecimal(),
        duration = movie.runtimeMinutes,
        posterImageURL = movie.posterImageURL,
        date = movie.releaseDate.toDDMMYYYYFormat(),
        isSaved = this@toMovieDetailsStateUpdate.isSaved,
        savedListId = this@toMovieDetailsStateUpdate.listId ?: -1L,
        categories = movie.genres.toCategoryUiStateList(),
        isHasTrailer = movie.trailerURL.isNotEmpty(),
        selectedMovieId = movie.id
    )
}

private fun List<Genre>.toCategoryUiStateList(): List<MovieDetailsState.CategoryUiState> {
    return map { genre ->
        MovieDetailsState.CategoryUiState(
            id = genre.id,
            name = genre.name
        )
    }
}