package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.repository.ContinueWatchingRepository

class AddContinueWatchingUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(continueWatching: ContinueWatching) {
        continueWatchingRepository.addContinueWatching(continueWatching)
    }
}