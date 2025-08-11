package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun MyRatingTvShowResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = results?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

fun MyRatingTvShowResponse.TvShowItem.toDto(): TvShowDto {
    return TvShowDto(
        id = id ?: 0L,
        title = name ?: originalName ?: "Unknown Title",
        genres = genreIds?.map { fakeNameForTvShowGenreMapper(it) } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = rating,
        releaseDate = firstAirDate ?: "Unknown Date",
        overview = overview ?: "",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = "",
        headerImagesURLs = backdropPath?.let { listOf("https://image.tmdb.org/t/p/w780$it") }
            ?: emptyList(),
        numberOfSeasons = 0
    )
}

private fun fakeNameForTvShowGenreMapper(genreId: Long?): GenreDto {
    return GenreDto(
        id = genreId ?: 0L,
        name = "",
        type = GenreDto.GenreType.MOVIE
    )
}