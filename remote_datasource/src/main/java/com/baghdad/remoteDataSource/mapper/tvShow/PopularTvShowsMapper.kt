package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto

fun PopularTvShowsResponse.toTvShowDtos(): List<TvShowDto> {
    return results.orEmpty().mapNotNull { it.toTvShowDtoIfValid() }
}

private fun PopularTvShowsResponse.Result?.toTvShowDtoIfValid(): TvShowDto? {
    return this?.takeIf { id != null }?.toTvShowDto()
}

private fun PopularTvShowsResponse.Result.toTvShowDto(): TvShowDto {
    return TvShowDto(
        id = id ?: -1L,
        title = name.orEmpty(),
        genres = genreIds.toTvShowGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0,
        releaseDate = firstAirDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = "",
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        headerImagesURLs = emptyList(),
        numberOfSeasons = 0
    )
}

private fun List<Long?>?.toTvShowGenreDtos(): List<GenreDto> =
    this
        ?.mapNotNull { genreId ->
            genreId?.let {
                GenreDto(
                    id = it,
                    name = "",
                    type = GenreDto.GenreType.TV_SHOW,
                )
        }
    }.orEmpty()
