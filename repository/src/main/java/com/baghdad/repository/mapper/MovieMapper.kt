package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import kotlinx.datetime.LocalDate

fun MovieDto.toEntity(): Movie =
    Movie(
        id = id,
        title = title,
        genres = genres.map { it.toEntity() },
        averageRating = imdbRating,
        userRating = userRating,
        releaseDate = releaseDate.toLocalDateOrDefault(),
        overview = overview,
        posterImageURL = posterPictureURL,
        trailerURL = trailerURL,
        runtimeMinutes = runtimeMinutes,
    )

fun MovieDto.toSavableMovie(
    isSaved: Boolean,
    listId: Long? = null,
): SavedMovie =
    SavedMovie(
        movie = toEntity(),
        isSaved = isSaved,
        listId = listId
    )

fun Movie.toDto(): MovieDto =
    MovieDto(
        id = id,
        title = title,
        genres = genres.map { it.toDto() },
        imdbRating = averageRating,
        userRating = userRating,
        releaseDate = releaseDate.toString(),
        overview = overview,
        posterPictureURL = posterImageURL,
        trailerURL = trailerURL,
        runtimeMinutes = runtimeMinutes,
    )

fun Genre.toDto(): GenreDto =
    GenreDto(
        id = id,
        name = name,
        type = GenreDto.GenreType.MOVIE,
    )


fun MovieDto.toMedia(): RatedMedia {
    return RatedMedia(
        id = id,
        userRating = userRating?.toInt(),
        posterImageURL = posterPictureURL,
        contentType = RatedMedia.ContentType.MOVIE
    )
}

fun SavedMovie.toDto(): SavedMovie {
    return SavedMovie(
        movie = movie,
        isSaved = isSaved,
        listId = listId
    )
}

/**
 * note: here we use typealias cuz the type string is not always format like a date
 */
typealias DateString = String

private fun DateString?.toLocalDateOrDefault(): LocalDate =
    this?.takeIf { it.isNotBlank() }
        ?.let { LocalDate.parse(it) }
        ?: LocalDate(1990, 1, 1)
