package com.baghdad.remoteDataSource.mapper.tvShow


import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto


fun TopRatedTvShowSearchResponse.toPagedTvShowDtos() = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toTvShowDto() } ?: emptyList(),
    nextKey = getNextKey(page, totalPages),
    prevKey = getPreviousKey(page)
)


private fun TopRatedTvShowSearchResponse.Result.toTvShowDto(
    numberOfSeasons: Int = 1
): TvShowDto {
    val tvShowGenres = genreIds?.map { GenreDto(it?.toLong() ?: 0L, "", GenreDto.GenreType.TV_SHOW) } ?: emptyList()
    return TvShowDto(
        id = id ?: 0L,
        title = title.orEmpty(),
        genres = tvShowGenres,
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