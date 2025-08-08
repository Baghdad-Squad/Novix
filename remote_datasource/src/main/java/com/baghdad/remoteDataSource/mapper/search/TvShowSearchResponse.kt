package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TvShowSearchResponse.toPagedTvShowDtos(genres: List<GenreDto>) = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toTvShowDto(genres) } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

private fun TvShowSearchResponse.Result.toTvShowDto(
    genres: List<GenreDto> = emptyList(),
    numberOfSeasons: Int = 1
): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.title.orEmpty(),
        genres = filterGenres(this.genreIds ?: emptyList<Int>(), genres),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = 0,
        releaseDate = this.releaseDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        numberOfSeasons = numberOfSeasons,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}

private fun filterGenres(
    genreIds: List<Int?>,
    genres: List<GenreDto>
): List<GenreDto> {
    return genres.filter { genre ->
        genreIds.contains(genre.id.toInt())
    }
}