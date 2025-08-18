package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.person.Actor
import javax.inject.Inject

class SearchActorsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String, page: Int): PagedResult<Actor> {
        return searchRepository.searchActorsByName(name = query, page =  page)
    }
}
