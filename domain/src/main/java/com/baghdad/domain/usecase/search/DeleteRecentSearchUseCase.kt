package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import javax.inject.Inject

class DeleteRecentSearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(id: Long) {
        searchRepository.deleteRecentSearchById(id)
    }
}