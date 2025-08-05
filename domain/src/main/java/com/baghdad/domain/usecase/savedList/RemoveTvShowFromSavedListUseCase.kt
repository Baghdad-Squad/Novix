package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class RemoveTvShowFromSavedListUseCase @Inject constructor (
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, tvShowId: Long) {
        return savedListRepository.removeTvShowFromSavedList(listId, tvShowId)
    }
}