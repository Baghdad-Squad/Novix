package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCurrentContinueWatchingTvShowGenres @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
    private val getGenresUseCase: GetGenresUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<List<Genre>> =
        continueWatchingRepository.getAllContinueWatchingTvShows()
            .mapLatest { tvShows ->
                val tvShowGenres = getGenresUseCase.getTvShowGenres()
                val genreIdSet = tvShows.flatMap { it.genreIds }.toSet()
                tvShowGenres.filter { it.id in genreIdSet }
            }
}
