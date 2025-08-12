package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TvShowSearchResponse.toPagedTvShowDtos(genres: List<GenreDto>): PagedResultDto<TvShowDto> =
    PagedResultDto(
        data = toTvShowDtos(genres),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )

private fun TvShowSearchResponse.toTvShowDtos(genres: List<GenreDto>): List<TvShowDto> =
    results.orEmpty().mapNotNull {
        it.toTvShowDtoIfValid(genres)
    }

private fun TvShowSearchResponse.Result?.toTvShowDtoIfValid(genres: List<GenreDto>): TvShowDto? =
    this
        ?.takeIf {
            id != null
        }?.toTvShowDto(genres)

private fun TvShowSearchResponse.Result.toTvShowDto(
    genres: List<GenreDto>,
    numberOfSeasons: Int = 1
): TvShowDto {
    return TvShowDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = genreIds.filterMatchingGenres(genres),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0,
        releaseDate = releaseDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        numberOfSeasons = numberOfSeasons,
        trailerURL = "",
        headerImagesURLs = emptyList(),
    )
}

private fun List<Long?>?.filterMatchingGenres(availableGenres: List<GenreDto>): List<GenreDto> {
    val validGenreIds = this?.filterNotNull()?.toSet() ?: return emptyList()
    return availableGenres.filter { genre -> validGenreIds.contains(genre.id) }
}