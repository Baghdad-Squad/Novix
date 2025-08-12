package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun MyRatingTvShowResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = toTvShowDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun MyRatingTvShowResponse.toTvShowDtos(): List<TvShowDto> {
    return results.orEmpty().mapNotNull { it.toTvShowDtoIfValid() }
}

private fun MyRatingTvShowResponse.TvShowItem?.toTvShowDtoIfValid(): TvShowDto? {
    return this?.takeIf { id != null }?.toTvShowDto()
}

private fun MyRatingTvShowResponse.TvShowItem.toTvShowDto(): TvShowDto {
    return TvShowDto(
        id = id ?: -1L,
        title = name ?: originalName.orEmpty(),
        genres = genreIds.toTvShowGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = rating,
        releaseDate = firstAirDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        headerImagesURLs = listOf(getImageUrlFromPath(backdropPath)),
        numberOfSeasons = 0,
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
