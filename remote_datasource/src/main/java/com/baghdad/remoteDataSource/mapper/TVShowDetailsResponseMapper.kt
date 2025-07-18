package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.GenreDto.GenreType
import com.baghdad.repository.model.TvShowDto

fun TVShowDetailsResponse.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name.orEmpty(),
        genres = this.genres?.map {
            GenreDto(
                id = it.id?.toLong() ?: 0L,
                name = it.name.orEmpty(),
                type = GenreType.TV_SHOW
            )
        } ?: emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        releaseDate = this.firstAirDate ?: "0001-01-01",
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath.orEmpty(),
        numberOfSeasons = this.numberOfSeasons ?: 0
    )
}
