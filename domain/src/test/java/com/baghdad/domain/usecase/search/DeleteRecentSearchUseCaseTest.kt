package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteRecentSearchUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val deleteRecentSearchUseCase = DeleteRecentSearchUseCase(searchRepository)

    @Test
    fun `deleteRecentSearchUseCase should delete recent search by id successfully`() = runTest {
        coEvery { searchRepository.deleteRecentSearchById(id) } returns Unit

        deleteRecentSearchUseCase(id)

        coVerify(exactly = 1) { searchRepository.deleteRecentSearchById(id) }
    }

    companion object {
        val id = 123L
    }
}