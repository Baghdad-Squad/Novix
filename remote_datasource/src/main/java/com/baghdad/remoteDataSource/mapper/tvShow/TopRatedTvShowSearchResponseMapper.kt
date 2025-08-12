package com.baghdad.remoteDataSource.mapper.tvShow


import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TopRatedTvShowSearchResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = toTvShowDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )
}

private fun TopRatedTvShowSearchResponse.toTvShowDtos(): List<TvShowDto> {
    return results.orEmpty().mapNotNull { it.toTvShowDtoIfValid() }
}

private fun TopRatedTvShowSearchResponse.Result?.toTvShowDtoIfValid(): TvShowDto? {
    return this?.takeIf { id != null }?.toTvShowDto()
}

private fun TopRatedTvShowSearchResponse.Result.toTvShowDto(numberOfSeasons: Int = 1): TvShowDto {
    return TvShowDto(
        id = id ?: 0L,
        title = title.orEmpty(),
        genres = genreIds.toTvShowGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0,
        releaseDate = releaseDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        numberOfSeasons = numberOfSeasons,
        trailerURL = "",
        headerImagesURLs = emptyList()
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
