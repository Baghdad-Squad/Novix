package com.baghdad.repository

import com.baghdad.domain.result.SearchResult
import com.baghdad.entity.search.RecentSearch
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class SearchRepositoryImplTest {
    private val searchRepository = SearchRepositoryImpl()

    @Test
    fun searchByName() = runTest {
        assertThat(searchRepository.searchByName("query")).isEqualTo(
            SearchResult(
                actors = emptyList(),
                movies = emptyList(),
                tvShows = emptyList()
            )
        )

    }

    @Test
    fun getRecentSearches() = runTest {
        assertThat(searchRepository.getRecentSearches()).isEqualTo(emptyList<RecentSearch>())

    }

    @Test
    fun deleteRecentSearchById() = runTest {
        assertDoesNotThrow {
            searchRepository.deleteRecentSearchById(1)
        }
    }

    @Test
    fun deleteAllRecentSearches() = runTest {
        assertThat(searchRepository.getRecentSearches()).isEqualTo(emptyList<RecentSearch>())
    }
}