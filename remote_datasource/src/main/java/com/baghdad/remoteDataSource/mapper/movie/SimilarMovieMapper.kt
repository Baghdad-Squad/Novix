package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto

fun SimilarMovieResponse.toPagedMovieDtos() = PagedResultDto(
    data = results?.mapNotNull {
        it.toDto(
            genreIds = it.genreIds
        )
    } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)