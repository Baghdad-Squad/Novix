package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.domain.usecase.genre.GetGenresUseCase
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentContinueWatchingMovieGenres @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository,
    private val getGenresUseCase: GetGenresUseCase,
) {
     operator fun invoke(): Flow<List<Genre>> = flow {
        val movieGenres = getGenresUseCase.getMovieGenres()
        val movies = continueWatchingRepository.getAllContinueWatchingMovies().first()
        val filteredGenres = movieGenres.filter { genre ->
            genre.id in movies.flatMap { it.genreIds }.toSet()
        }
        emit(filteredGenres)
    }
}
