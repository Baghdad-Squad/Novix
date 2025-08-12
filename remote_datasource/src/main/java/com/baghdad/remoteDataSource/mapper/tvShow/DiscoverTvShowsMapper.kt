package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

fun TvShowResponse.toPagedTvShowDtos(): PagedResultDto<TvShowDto> {
    return PagedResultDto(
        data = toTvShowDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun TvShowResponse.toTvShowDtos(): List<TvShowDto> = results.orEmpty().mapNotNull { it.toTvShowDtoIfValid() }

private fun TVShowDetailsResponse?.toTvShowDtoIfValid(): TvShowDto? = this?.takeIf { id != null }?.toDto()
