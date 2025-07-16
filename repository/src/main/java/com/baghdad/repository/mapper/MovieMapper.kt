package com.baghdad.repository.mapper

import com.baghdad.entity.media.Movie
import com.baghdad.repository.model.MovieDto
import kotlinx.datetime.LocalDate

fun MovieDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.map { it.toEntity() },
        averageRating = imdbRating,
        userRating = userRating,
        releaseDate = releaseDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
            ?: LocalDate(1990, 1, 1),
        overview = overview,
        posterImageURL = posterPictureURL,
        runtimeMinutes = runtimeMinutes
    )
}
