package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.testHelper.getTestRecentSearches
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRecentSearchesUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        getRecentSearchesUseCase = GetRecentSearchesUseCase(searchRepository)
    }

    @Test
    fun `should return flow with multiple recent search items`() = runTest {
        val searches = getTestRecentSearches(5)
        coEvery { searchRepository.getRecentSearches() } returns flowOf(searches)

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(5)
        assertThat(result.first()).containsExactlyElementsIn(searches).inOrder()
    }

    @Test
    fun `should return flow with empty list when repository returns empty list`() = runTest {
        coEvery { searchRepository.getRecentSearches() } returns flowOf(emptyList())

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should handle multiple flow emissions from repository`() = runTest {
        val firstEmission = getTestRecentSearches(2)
        val secondEmission = getTestRecentSearches(3)
        coEvery { searchRepository.getRecentSearches() } returns flowOf(
            firstEmission,
            secondEmission
        )

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(2)
        assertThat(result[0]).containsExactlyElementsIn(firstEmission).inOrder()
        assertThat(result[1]).containsExactlyElementsIn(secondEmission).inOrder()
    }

    @Test
    fun `should propagate exception when repository throws`() = runTest {
        val exception = RuntimeException()
        coEvery { searchRepository.getRecentSearches() } throws exception

        val resultException = runCatching { getRecentSearchesUseCase().toList() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}
