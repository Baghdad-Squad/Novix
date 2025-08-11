package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.GenreDto.GenreType
import com.baghdad.repository.model.TvShowDto

fun TVShowDetailsResponse.toDto(): TvShowDto {
    return TvShowDto(
        id = id ?: 0L,
        title = name.orEmpty(),
        genres = genres?.map {
            GenreDto(
                id = it.id?: 0L,
                name = it.name.orEmpty(),
                type = GenreType.TV_SHOW
            )
        } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = firstAirDate ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = "https://image.tmdb.org/t/p/w500" + posterPath.orEmpty(),
        numberOfSeasons = numberOfSeasons ?: 0,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}
