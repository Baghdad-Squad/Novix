package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.testHelper.getSampleActor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTrendingActorsUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getTrendingActorsUseCase: GetTrendingActorsUseCase

    @BeforeEach
    fun setUp() {
        actorRepository = mockk()
        getTrendingActorsUseCase = GetTrendingActorsUseCase(actorRepository)
    }

    @Test
    fun `invoke() should return paged result from repository`() = runTest {
        coEvery { actorRepository.getTrendingActors(PAGE) } returns EXPECTED_RESULT

        val result = getTrendingActorsUseCase(PAGE)

        assertThat(result).isEqualTo(EXPECTED_RESULT)
        coVerify(exactly = 1) { actorRepository.getTrendingActors(PAGE) }
    }

    companion object {
        private const val PAGE = 1

        private fun sampleActor() = getSampleActor()

        private val EXPECTED_RESULT = PagedResult(
            data = listOf(sampleActor()),
            nextPage = 2,
            prevPage = null
        )
    }
}