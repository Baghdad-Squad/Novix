package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.tvShow.TvShowItemDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.GenreDto.GenreType
import com.baghdad.repository.model.TvShowDto

fun TvShowItemDto.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name.orEmpty(),
        genres = this.genreIds?.map { genreId ->
            GenreDto(
                id = genreId.toLong(),
                name = "",
                type = GenreType.TV_SHOW
            )
        } ?: emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        releaseDate = this.firstAirDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath.orEmpty(),
        numberOfSeasons = 1
    )
}
