package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.repository.SavedListRepository

class GetSavedListDetailsUseCase(
    private val savedListRepository: SavedListRepository,
) {
    suspend operator fun invoke(listId: Long): SavedListDetails = savedListRepository.getSavedListDetails(listId)
}