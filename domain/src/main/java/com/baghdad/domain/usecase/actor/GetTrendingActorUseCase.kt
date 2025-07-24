package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor

class GetTrendingActorUseCase(
    private val actorRepository: ActorRepository

) {
    suspend operator fun invoke(page: Int): PagedResult<Actor> {
        return actorRepository.getTrendingActor(page)

    }
}