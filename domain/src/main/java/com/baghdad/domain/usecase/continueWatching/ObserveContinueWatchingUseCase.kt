package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository

class ObserveContinueWatchingUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository,
) {
    operator fun invoke() = continueWatchingRepository.observeContinueWatching()
}
