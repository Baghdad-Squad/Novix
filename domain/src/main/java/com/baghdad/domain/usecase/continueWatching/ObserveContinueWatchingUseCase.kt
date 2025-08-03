package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository

class ObserveContinueWatchingUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository,
) {
    suspend operator fun invoke() = continueWatchingRepository.observeContinueWatching()
}
