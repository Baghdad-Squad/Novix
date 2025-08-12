package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorMoviesUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(actorId: Long): List<SavedMovie> {
        return actorRepository.getActorMovies(actorId = actorId)
    }
}