package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteAllRecentSearchesUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val deleteAllRecentSearchesUseCase = DeleteAllRecentSearchesUseCase(searchRepository)


    @Test
    fun `deleteAllRecentSearchesUseCase should delete all recent searches successfully`() =
        runTest {
            coEvery { searchRepository.deleteAllRecentSearches() } returns Unit

        deleteAllRecentSearchesUseCase()

        coVerify(exactly = 1) { searchRepository.deleteAllRecentSearches() }
    }
}