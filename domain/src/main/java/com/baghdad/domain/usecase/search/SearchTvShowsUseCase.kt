package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class SearchTvShowsUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int
    ): PagedResult<TvShow> {
        val searchResults = searchRepository.searchTvShowsByName(title = query, page = page)

        val filteredShows = searchResults.data

        return searchResults.copy(data = filteredShows)
    }
}
