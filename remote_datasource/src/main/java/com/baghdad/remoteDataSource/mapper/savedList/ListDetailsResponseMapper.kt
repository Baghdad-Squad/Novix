package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto

fun ListDetailsResponse.toSavedListDetailsDto(): SavedListDetailsDto =
    SavedListDetailsDto(
        savedList =
            SavedListDto(
                id = id ?: -1L,
                name = name.orEmpty(),
                itemCount = itemCount ?: 0,
            ),
        pagedItems =
            PagedResultDto(
                data = items.orEmpty()
                        .filterNotNull()
                        .mapNotNull { it.toSavedListItemDto() },
                nextKey = getNextKey(page, totalPages),
                prevKey = getPreviousKey(page),
            ),
    )

private fun ListDetailsResponse.Item.toSavedListItemDto(): SavableMovieDto? {
    return SavableMovieDto(
        movie =
            MovieDto(
                id = id ?: 0L,
                title = title ?: originalTitle ?: return null,
                genres = emptyList(),
                imdbRating = voteAverage ?: 0.0,
                userRating = null,
                releaseDate = releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
                overview = overview ?: "",
                posterPictureURL = "https://image.tmdb.org/t/p/w500" + posterPath.orEmpty(),
                trailerURL = "",
                runtimeMinutes = 0,
            ),
            isSaved = false,
        listId = null,
    )
}