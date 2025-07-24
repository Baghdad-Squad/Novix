package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse.TrendingTvShow
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TrendingTvShowsResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = this.results?.mapNotNull { it?.toTvShowDto() } ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

fun TrendingTvShow.toTvShowDto(): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        genres = genreIds?.map { GenreDto(it.toLong(), "", GenreDto.GenreType.TV_SHOW) }
            ?: emptyList(),
        posterPictureURL = "https://image.tmdb.org/t/p/w500" + this.posterPath.orEmpty(),
        title = this.name ?: "",
        overview = this.overview ?: "",
        releaseDate = this.firstAirDate ?: "",
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        trailerURL = "",
        headerImagesURLs = emptyList(),
        numberOfSeasons = 0
    )
}