package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class AddTvShowToSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, tvShowId: Long) {
        return savedListRepository.addTvShowToSavedList(listId, tvShowId)
    }
}