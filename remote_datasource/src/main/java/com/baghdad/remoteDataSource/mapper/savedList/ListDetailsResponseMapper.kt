package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto

fun ListDetailsResponse.toSavedListDetailsDto(): SavedListDetailsDto =
    SavedListDetailsDto(
        savedList = toSavedListDto(),
        pagedItems = toPagedSavableMovies(),
    )

private fun ListDetailsResponse.toSavedListDto(): SavedListDto =
    SavedListDto(
        id = id ?: -1L,
        name = name.orEmpty(),
        itemCount = itemCount ?: 0,
    )

private fun ListDetailsResponse.toPagedSavableMovies(): PagedResultDto<SavableMovieDto> =
    PagedResultDto(
        data = toSavableMovieDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )

private fun ListDetailsResponse.toSavableMovieDtos(): List<SavableMovieDto> =
    items.orEmpty().filterNotNull().mapNotNull {
        it.toSavableMovieDto()
    }

private fun ListDetailsResponse.Item.toSavableMovieDto(): SavableMovieDto? {
    val movieTitle = title ?: originalTitle ?: return null

    return SavableMovieDto(
        movie = toMovieDto(movieTitle),
        isSaved = false,
        listId = null,
    )
}

private fun ListDetailsResponse.Item.toMovieDto(movieTitle: String): MovieDto =
    MovieDto(
        id = id ?: 0L,
        title = movieTitle,
        genres = emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = releaseDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        runtimeMinutes = 0,
    )
