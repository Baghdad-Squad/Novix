package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.SearchRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
    ): PagedResult<SavableMovie> {
        val searchResults = searchRepository.searchMoviesByTitle(query, page)

        val filteredMovies = searchResults.data

        return searchResults.copy(data = filteredMovies)
    }
}
