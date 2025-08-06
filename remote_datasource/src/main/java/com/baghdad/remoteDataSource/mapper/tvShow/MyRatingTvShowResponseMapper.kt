package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun MyRatingTvShowResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data =
            this.results?.mapNotNull { it.takeIf { it.id != null }?.toDto() }
                ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

fun MyRatingTvShowResponse.TvShowItem.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name ?: this.originalName ?: "Unknown Title",
        genres = this.genreIds?.map { fakeNameForTvShowGenreMapper(it) } ?: emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = this.rating?.toDouble(),
        releaseDate = this.firstAirDate ?: "Unknown Date",
        overview = this.overview ?: "",
        posterPictureURL = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = "",
        headerImagesURLs = this.backdropPath?.let { listOf("https://image.tmdb.org/t/p/w780$it") }
            ?: emptyList(),
        numberOfSeasons = 0
    )
}

private fun fakeNameForTvShowGenreMapper(genreId: Long): GenreDto {
    return GenreDto(
        id = genreId,
        name = "",
        type = GenreDto.GenreType.MOVIE
    )
}