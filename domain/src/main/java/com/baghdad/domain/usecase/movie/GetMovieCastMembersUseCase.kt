package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieCastMembersUseCase @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) = movieRepository.getMovieCastMembers(movieId)
}