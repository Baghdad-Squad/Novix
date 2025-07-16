package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.entity.person.Actor

class SearchActorsUseCase(
    private val searchActorsByName: ActorRepository
) {
    suspend operator fun invoke(query: String): List<Actor> {
        return searchActorsByName.searchActorsByName(query)
    }
}
