package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SavedListRepository

class GetSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(
        page: Int,
        sessionId: String
    ): PagedResult<com.baghdad.entity.savedList.SavedList> {
        return savedListRepository.getSavedLists(page, sessionId)
    }
}