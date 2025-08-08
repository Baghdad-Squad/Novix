package com.baghdad.repository.mapper

import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.model.savedList.SavableMovie
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
        releaseDate =
            releaseDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
                ?: LocalDate(1990, 1, 1),
        overview = overview,
        posterImageURL = posterPictureURL,
        trailerURL = trailerURL,
        runtimeMinutes = runtimeMinutes,
    )

fun MovieDto.toSavableMovie(
    isSaved: Boolean,
    listId: Long? = null,
): SavableMovie =
    SavableMovie(
        movie =
            Movie(
                id = id,
                title = title,
                genres = genres.map { it.toEntity() },
                averageRating = imdbRating,
                userRating = userRating,
                releaseDate =
                    releaseDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
                        ?: LocalDate(1990, 1, 1),
                overview = overview,
                posterImageURL = posterPictureURL,
                trailerURL = trailerURL,
                runtimeMinutes = runtimeMinutes,
            ),
        isSaved = isSaved,
        listId = listId,
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

fun MovieDto.toMedia(): RatedMedia{
    return RatedMedia(
        id = id,
        userRating = userRating?.toInt(),
        posterImageURL = posterPictureURL,
        contentType = RatedMedia.ContentType.MOVIE
    )
}