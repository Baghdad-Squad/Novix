package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class CreateSavedListUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(title: String) {
        savedListRepository.createSavedList(title = title.trim())
    }
}