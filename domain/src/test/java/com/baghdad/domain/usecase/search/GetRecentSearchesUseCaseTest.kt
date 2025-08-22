package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.testHelper.getTestRecentSearches
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetRecentSearchesUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val getRecentSearchesUseCase = GetRecentSearchesUseCase(searchRepository)

    @Test
    fun `getRecentSearchesUseCase should return sorted recent searches by searchedAt descending`() =
        runTest {
        val unsortedSearches = getTestRecentSearches(size = 5).shuffled()
        val expectedSorted = unsortedSearches.sortedByDescending { it.searchedAt }

        coEvery { searchRepository.getRecentSearches() } returns flowOf(unsortedSearches)

        val result = getRecentSearchesUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).containsExactlyElementsIn(expectedSorted).inOrder()
    }
}