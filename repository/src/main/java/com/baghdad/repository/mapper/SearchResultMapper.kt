package com.baghdad.repository.mapper

import com.baghdad.domain.result.SearchResult
import com.baghdad.repository.model.SearchResultDto

internal fun SearchResultDto.toEntity(): SearchResult {
    return SearchResult(
        actors = actors.map { it.toEntity() },
        movies = movies.map { it.toEntity() },
        tvShows = tvShows.map { it.toEntity() }
    )
}
