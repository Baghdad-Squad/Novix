package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto

fun TVShowDetailsResponse.toDto(): TvShowDto {
    return TvShowDto(
        id = id ?: 0L,
        title = name.orEmpty(),
        genres = genres.toTvShowGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = firstAirDate ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        numberOfSeasons = numberOfSeasons ?: 0,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}

private fun List<TVShowDetailsResponse.Genre>?.toTvShowGenreDtos(): List<GenreDto> {
    return this
        ?.map { genre ->
            GenreDto(
                id = genre.id ?: 0L,
                name = genre.name.orEmpty(),
                type = GenreDto.GenreType.TV_SHOW,
            )
        }.orEmpty()
}
