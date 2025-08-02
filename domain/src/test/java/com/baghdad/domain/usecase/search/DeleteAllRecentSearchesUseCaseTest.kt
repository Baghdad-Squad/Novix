package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteAllRecentSearchesUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var deleteAllRecentSearchesUseCase: DeleteAllRecentSearchesUseCase

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        deleteAllRecentSearchesUseCase = DeleteAllRecentSearchesUseCase(searchRepository)
    }

    @Test
    fun `invoke() should delete all recent searches successfully`() = runTest {
        deleteAllRecentSearchesUseCase()

        coVerify(exactly = 1) { searchRepository.deleteAllRecentSearches() }
    }

    @Test
    fun `invoke() should propagate exception when repository throws exception`() = runTest {
        val exception = RuntimeException()
        coEvery { searchRepository.deleteAllRecentSearches() } throws exception

        val resultException = runCatching { deleteAllRecentSearchesUseCase() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}