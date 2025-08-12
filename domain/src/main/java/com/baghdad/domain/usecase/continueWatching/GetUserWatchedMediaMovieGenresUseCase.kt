package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContinueWatchingMovieGenresUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<List<Genre>> {
        return continueWatchingRepository.getUsedMovieGenres()
    }
}