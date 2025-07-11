package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository

class DeleteRecentSearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(id: Long) {
        return searchRepository.deleteRecentSearchById(id)
    }
}