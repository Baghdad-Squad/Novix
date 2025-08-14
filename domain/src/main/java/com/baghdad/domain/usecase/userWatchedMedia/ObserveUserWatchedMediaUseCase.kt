package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.repository.UserWatchedMediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserWatchedMediaUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository,
) {
    suspend operator fun invoke(): Flow<List<UserWatchedMedia>> {
        return userWatchedMediaRepository.observeUserWatchedMedia()
    }
}
