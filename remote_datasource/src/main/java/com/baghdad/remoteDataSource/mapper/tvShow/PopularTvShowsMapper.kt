package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto

fun PopularTvShowsResponse.toTvShowDtos(): List<TvShowDto> {
    return results?.mapNotNull { it?.takeIf { it.id != null }?.toDto() } ?: emptyList()
}

fun PopularTvShowsResponse.Result.toDto(): TvShowDto {
    return TvShowDto(
        id = id ?: 0,
        title = name ?: "",
        genres = genreIds?.map {
            GenreDto(
                id = it ?: 0,
                name = "",
                type = GenreDto.GenreType.TV_SHOW
            )
        } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0,
        releaseDate = firstAirDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = "",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = "",
        headerImagesURLs = emptyList(),
        numberOfSeasons = 0
    )
}

