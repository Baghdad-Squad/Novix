package com.baghdad.domain.usecase.actorDetails

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.media.Movie

class GetActorMoviesUseCase(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<Movie> {
        return actorRepository.getActorMovies(actorId)
    }
}