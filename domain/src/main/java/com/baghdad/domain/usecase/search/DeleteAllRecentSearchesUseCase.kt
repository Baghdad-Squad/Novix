package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository

class DeleteAllRecentSearchesUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() {
        return searchRepository.deleteAllRecentSearches()
    }
}