package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedListCountUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    operator fun invoke(): Flow<Int> {
        return savedListRepository.getSavedListCount()
    }
}