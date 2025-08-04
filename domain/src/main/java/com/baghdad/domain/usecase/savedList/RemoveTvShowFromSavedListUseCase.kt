package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class RemoveTvShowFromSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, tvShowId: Long) {
        return savedListRepository.removeTvShowFromSavedList(listId, tvShowId)
    }
}