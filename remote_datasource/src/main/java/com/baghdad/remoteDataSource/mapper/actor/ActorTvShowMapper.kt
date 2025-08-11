package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.repository.model.TvShowDto

fun ActorTvShowsResponse.ActorTvShowDto.toDto(
): TvShowDto {
    return TvShowDto(
        id = id ?: 0L,
        title = name ?: originalName.orEmpty(),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = firstAirDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = "https://image.tmdb.org/t/p/w500$posterPath",
        numberOfSeasons = 0,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}
