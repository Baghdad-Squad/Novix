package com.baghdad.repository.mapper

import com.baghdad.entity.media.TvShow
import com.baghdad.repository.model.TvShowDto
import kotlinx.datetime.LocalDate

fun TvShowDto.toEntity(): TvShow {
    return TvShow(
        id = id,
        title = title,
        genres = genres.map { it.toEntity() },
        averageRating = imdbRating,
        userRating = userRating,
        releaseDate = releaseDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
            ?: LocalDate(1990, 1, 1),
        overview = overview,
        posterImageURL = posterPictureURL,
        numberOfSeasons = numberOfSeasons,
        headerPictures = headerPictures
    )
}
