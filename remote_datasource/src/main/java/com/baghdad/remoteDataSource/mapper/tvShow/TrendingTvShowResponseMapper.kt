package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse.TrendingTvShow
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TrendingTvShowsResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = toTvShowDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun TrendingTvShowsResponse.toTvShowDtos(): List<TvShowDto> = results.orEmpty().mapNotNull { it.toTvShowDtoIfValid() }

private fun TrendingTvShow?.toTvShowDtoIfValid(): TvShowDto? = this?.takeIf { id != null }?.toTvShowDto()

private fun TrendingTvShow.toTvShowDto(): TvShowDto =
    TvShowDto(
        id = id ?: 0L,
        title = name.orEmpty(),
        genres = genreIds.toTvShowGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = firstAirDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        headerImagesURLs = emptyList(),
        numberOfSeasons = 0
    )

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
