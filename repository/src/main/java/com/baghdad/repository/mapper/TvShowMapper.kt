package com.baghdad.repository.mapper

import com.baghdad.domain.model.userRating.RatedMedia
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
        releaseDate = releaseDate
            .takeIf { it.isNotBlank() }
            ?.let { LocalDate.parse(input = it) }
            ?: LocalDate(year = 1990, month = 1, day = 1),
        overview = overview,
        posterImageURL = posterPictureURL,
        headerImagesURLs = headerImagesURLs,
        trailerURL = trailerURL,
        numberOfSeasons = numberOfSeasons
    )
}

fun TvShowDto.toMedia(): RatedMedia {
    return RatedMedia(
        id = id,
        userRating = userRating,
        posterImageURL = posterPictureURL,
        contentType = RatedMedia.ContentType.TV_SHOW
    )
}