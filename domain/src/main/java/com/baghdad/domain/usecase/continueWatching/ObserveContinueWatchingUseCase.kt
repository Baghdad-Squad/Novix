package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class ObserveContinueWatchingUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
) {
    operator fun invoke() = continueWatchingRepository.observeContinueWatching()
}
