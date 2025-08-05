package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentContinueWatchingTvShowGenres @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
    private val getGenresUseCase: GetGenresUseCase,
) {
    operator fun invoke(): Flow<List<Genre>> = flow {
        val tvShowGenres = getGenresUseCase.getTvShowGenres()
        val tvShows = continueWatchingRepository.getAllContinueWatchingTvShows().first()
        val filteredGenres = tvShowGenres.filter { genre ->
            genre.id in (tvShows.flatMap { it.genreIds }.toSet())
        }
        emit(filteredGenres)
    }
}
