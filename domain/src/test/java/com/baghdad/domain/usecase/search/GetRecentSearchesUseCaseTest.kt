package com.baghdad.domain.usecase.search

import com.baghdad.entity.search.RecentSearch
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetRecentSearchesUseCaseTest {
    private val getRecentSearchesUseCase = GetRecentSearchesUseCase()

    @Test
    fun dummyTest() = runTest {
        val result = getRecentSearchesUseCase().first()
        Truth.assertThat(result).isEqualTo(emptyList<RecentSearch>())
    }
}