package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.ActorRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTrendingActorsUseCaseTest {

    private val actorRepository = mockk<ActorRepository>()
    private val getTrendingActorsUseCase = GetTrendingActorsUseCase(actorRepository)

    @Test
    fun `getTrendingActorsUseCase should return paged result from repository`() = runTest {
        coEvery { actorRepository.getTrendingActors(page) } returns expectedActorsResult

        val result = getTrendingActorsUseCase(page)

        assertThat(result).isEqualTo(expectedActorsResult)
        coVerify(exactly = 1) { actorRepository.getTrendingActors(page) }
    }

    private companion object {
        val page = ActorMock.PAGE
        val actor = ActorMock.ACTOR

        val expectedActorsResult = PagedResult(
            data = ActorMock.ACTORS,
            nextPage = 2,
            prevPage = null
        )
    }
}