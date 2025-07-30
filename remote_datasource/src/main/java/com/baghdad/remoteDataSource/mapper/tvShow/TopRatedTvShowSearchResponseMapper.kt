package com.baghdad.remoteDataSource.mapper.tvShow


import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto


fun TopRatedTvShowSearchResponse.toPagedTvShowDtos() = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toTvShowDto() } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)


private fun TopRatedTvShowSearchResponse.Result.toTvShowDto(
    userRating: Double? = null,
    numberOfSeasons: Int = 1
): TvShowDto {
    val tvShowGenres = this.genreIds?.map { GenreDto(it?.toLong() ?: 0L, "", GenreDto.GenreType.TV_SHOW) } ?: emptyList()
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.title.orEmpty(),
        genres = tvShowGenres,
        imdbRating = this.voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = this.releaseDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        numberOfSeasons = numberOfSeasons,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}