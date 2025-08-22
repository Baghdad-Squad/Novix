package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.usecase.actor.ActorMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SearchActorsUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val searchActorsUseCase = SearchActorsUseCase(searchRepository)

    @Test
    fun `searchActorsUseCase should return actors matching query with pagination keys`() = runTest {
        coEvery { searchRepository.searchActorsByName(query, page) } returns actor

        val result = searchActorsUseCase(query, page)

        assertThat(result).isEqualTo(actor)
    }

    private companion object {
        val actor = ActorMock.ACTOR_RESULT
        val query = "John Doe"
        val page = 1
    }
}