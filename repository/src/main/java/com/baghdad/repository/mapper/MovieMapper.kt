package com.baghdad.repository.mapper

import com.baghdad.entity.media.Movie
import com.baghdad.repository.model.MovieDto
import kotlinx.datetime.LocalDate

fun MovieDto.toEntity(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.map { it.toEntity() },
        imdbRating = imdbRating,
        userRating = userRating,
        releaseDate = LocalDate.parse(releaseDate),
        overview = overview,
        cast = cast.map { it.toEntity() },
        posterPictureURL = posterPictureURL,
        backdropPicturesURLs = backdropPicturesURLs,
        runtimeMinutes = runtimeMinutes
    )
}
