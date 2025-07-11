package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteRecentSearchUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        deleteRecentSearchUseCase = DeleteRecentSearchUseCase(searchRepository)
    }

    @Test
    fun `should delete recent search by id successfully`() = runTest {
        val id = 123L

        deleteRecentSearchUseCase(id)

        coVerify(exactly = 1) { searchRepository.deleteRecentSearchById(id) }
    }

    @Test
    fun `should propagate exception when repository throws exception`() = runTest {
        val id = 456L
        val exception = RuntimeException("Something went wrong")
        coEvery { searchRepository.deleteRecentSearchById(id) } throws exception

        val resultException = runCatching { deleteRecentSearchUseCase(id) }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}