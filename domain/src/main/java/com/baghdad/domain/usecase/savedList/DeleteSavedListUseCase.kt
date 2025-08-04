package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class DeleteSavedListUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long) {
        savedListRepository.deleteSavedListById(listId)
    }
}