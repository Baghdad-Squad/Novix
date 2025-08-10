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
                id = this.id ?: -1L,
                name = this.name.orEmpty(),
                itemCount = this.itemCount ?: 0,
            ),
        pagedItems =
            PagedResultDto(
                data =
                    this.items
                        .orEmpty()
                        .filterNotNull()
                        .mapNotNull { it.toSavedListItemDto() },
                nextKey = getNextKey(this.page, this.totalPages),
                prevKey = getPreviousKey(this.page),
            ),
    )

private fun ListDetailsResponse.Item.toSavedListItemDto(): SavableMovieDto? {
    return SavableMovieDto(
        movie =
            MovieDto(
                id = this.id ?: return null,
                title = this.title ?: this.originalTitle ?: return null,
                genres = emptyList(),
                imdbRating = this.voteAverage ?: 0.0,
                userRating = null,
                releaseDate = this.releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
                overview = this.overview ?: "",
                posterPictureURL = "https://image.tmdb.org/t/p/w500" + this.posterPath.orEmpty(),
                trailerURL = "",
                runtimeMinutes = 0,
            ),
            isSaved = false,
        listId = null,
    )
}