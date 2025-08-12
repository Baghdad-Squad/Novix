package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserWatchedMediaMovieGenresUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<List<Genre>> {
        return userWatchedMediaRepository.getUsedMovieGenres()
    }
}