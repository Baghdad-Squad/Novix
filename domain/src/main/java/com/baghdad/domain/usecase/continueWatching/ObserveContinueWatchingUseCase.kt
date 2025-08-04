package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class ObserveContinueWatchingUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
) {
    suspend operator fun invoke() = continueWatchingRepository.observeContinueWatching()
}
