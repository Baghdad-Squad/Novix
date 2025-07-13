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
        releaseDate = LocalDate.parse(releaseDate),
        overview = overview,
        posterImageURL = posterPictureURL,
        backdropImagesURLs = backdropPicturesURLs,
        numberOfSeasons = numberOfSeasons
    )
}
