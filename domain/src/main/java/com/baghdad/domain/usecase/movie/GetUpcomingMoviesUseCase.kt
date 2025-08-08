package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Long?): List<SavableMovie> = movieRepository.getUpcomingMovies(genreId)
}
