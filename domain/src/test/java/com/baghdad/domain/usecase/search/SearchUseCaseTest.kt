package com.baghdad.domain.usecase.search

import com.baghdad.domain.request.SearchRequest
import com.baghdad.domain.result.SearchResult
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SearchUseCaseTest {
    private val searchUseCase = SearchUseCase()
    private val dummySearchRequest = SearchRequest("test")
    private val dummySearchResult = SearchResult(emptyList(), emptyList(), emptyList())

    @Test
    fun dummyTest() = runTest {
        Truth.assertThat(searchUseCase(dummySearchRequest)).isEqualTo(dummySearchResult)
    }
}