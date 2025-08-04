package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class RemoveMovieFormSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, movieItem: Long) {
        return savedListRepository.removeMovieFromSavedList(listId, movieItem)
    }
}