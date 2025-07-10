package com.baghdad.domain.usecase.search

import com.baghdad.domain.request.SearchRequest
import com.baghdad.domain.result.SearchResult

class SearchUseCase {
    suspend operator fun invoke(request: SearchRequest): SearchResult {
        //TODO("Not yet implemented")
        return SearchResult(
            emptyList(),
            emptyList(),
            emptyList()
        )
    }
}