package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository

class AddMovieToSavedListUseCase(
    private val savedListRepository: SavedListRepository
) {
    suspend operator fun invoke(listId: Long, movieId: Long) {
        return savedListRepository.addMovieToSavedList(listId, movieId)
    }
}