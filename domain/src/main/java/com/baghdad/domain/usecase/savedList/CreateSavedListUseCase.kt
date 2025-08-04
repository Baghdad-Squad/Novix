package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class CreateSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(title: String) {
        return savedListRepository.createSavedList(title)
    }
}