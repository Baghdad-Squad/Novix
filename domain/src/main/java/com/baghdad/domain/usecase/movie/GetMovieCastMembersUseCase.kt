package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.person.CastMember
import javax.inject.Inject

class GetMovieCastMembersUseCase @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): List<CastMember> {
        return movieRepository.getMovieCastMembers(movieId = movieId)
    }
}