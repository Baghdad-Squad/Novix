package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import javax.inject.Inject

class DeleteAllRecentSearchesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() {
        searchRepository.deleteAllRecentSearches()
    }
}