package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.SearchRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
    ): PagedResult<SavedMovie> {
        val searchResults = searchRepository.searchMoviesByTitle(title = query, page =  page)

        val filteredMovies = searchResults.data

        return searchResults.copy(data = filteredMovies)
    }
}
