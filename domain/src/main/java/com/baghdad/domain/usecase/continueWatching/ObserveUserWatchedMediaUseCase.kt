package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.UserWatchedMediaRepository
import javax.inject.Inject

class ObserveUserWatchedMediaUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository,
) {
    suspend operator fun invoke() = userWatchedMediaRepository.observeUserWatchedMedia()
}
