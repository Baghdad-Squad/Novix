package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class CreateSavedListUseCase(
    private val savedListUseCase: SavedListRepository
) {
    suspend operator fun invoke(title: String) {
        return savedListUseCase.createSavedList(title)
    }
}