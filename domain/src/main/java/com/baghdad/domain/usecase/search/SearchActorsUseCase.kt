package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.person.Actor

class SearchActorsUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String, page: Int?): PagedResult<Actor> {
        return searchRepository.searchActorsByName(query, page)
    }
}
