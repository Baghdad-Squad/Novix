package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import javax.inject.Inject

class RemoveMovieFromSavedListUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, movieId: Long) {
        return savedListRepository.removeMovieFromSavedList(listId = listId, movieId = movieId)
    }
}