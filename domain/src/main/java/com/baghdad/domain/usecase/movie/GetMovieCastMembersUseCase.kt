package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetMovieCastMembersUseCase(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) = movieRepository.getMovieCastMembers(movieId)
}