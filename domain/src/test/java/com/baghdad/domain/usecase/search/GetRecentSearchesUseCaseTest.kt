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
    fun `invoke() should return sorted recent searches by searchedAt descending`() = runTest {
        val unsortedSearches = getTestRecentSearches(size = 5).shuffled()
        val expectedSorted = unsortedSearches.sortedByDescending { it.searchedAt }

        coEvery { searchRepository.getRecentSearches() } returns flowOf(unsortedSearches)

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).containsExactlyElementsIn(expectedSorted).inOrder()
    }

    @Test
    fun `invoke() should return flow with empty list when repository returns empty list`() = runTest {
        coEvery { searchRepository.getRecentSearches() } returns flowOf(emptyList())

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `invoke() should handle multiple flow emissions and sort each`() = runTest {
        val firstEmission = getTestRecentSearches(3).shuffled()
        val secondEmission = getTestRecentSearches(2).shuffled()

        val sortedFirst = firstEmission.sortedByDescending { it.searchedAt }
        val sortedSecond = secondEmission.sortedByDescending { it.searchedAt }

        coEvery { searchRepository.getRecentSearches() } returns flowOf(
            firstEmission,
            secondEmission
        )

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(2)
        assertThat(result[0]).containsExactlyElementsIn(sortedFirst).inOrder()
        assertThat(result[1]).containsExactlyElementsIn(sortedSecond).inOrder()
    }

    @Test
    fun `invoke() should propagate exception when repository throws`() = runTest {
        val exception = RuntimeException()
        coEvery { searchRepository.getRecentSearches() } throws exception

        val resultException = runCatching {
            getRecentSearchesUseCase().toList()
        }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}