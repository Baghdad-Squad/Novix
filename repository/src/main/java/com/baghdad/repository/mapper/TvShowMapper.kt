package com.baghdad.repository.mapper

import com.baghdad.entity.media.TvShow
import com.baghdad.repository.model.TvShowDto
import kotlinx.datetime.LocalDate

fun TvShowDto.toEntity(): TvShow {
    return TvShow(
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
        numberOfSeasons = numberOfSeasons
    )
}
