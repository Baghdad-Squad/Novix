package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.Movie
import javax.inject.Inject

class GetActorMoviesUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<Movie> {
        return actorRepository.getActorMovies(actorId)
    }
}