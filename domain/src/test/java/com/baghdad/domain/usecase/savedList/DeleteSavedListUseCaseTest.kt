package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteSavedListUseCaseTest {

    lateinit var deleteSavedListUseCase: DeleteSavedListUseCase
    lateinit var savedListRepository: SavedListRepository
    val listId = 1L

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        deleteSavedListUseCase = DeleteSavedListUseCase(savedListRepository)
    }

    @Test
    fun `invoke() should delete the list when selected`() = runTest {
        // When
        coEvery { savedListRepository.deleteSavedListById(any()) } just Runs
        deleteSavedListUseCase.invoke(listId)

        // Then
        coVerify(exactly = 1) { savedListRepository.deleteSavedListById(listId) }
    }

    @Test
    fun `should propagate exception when repository throws an exception`() = runTest {
        // Given
        val listId = 200L
        coEvery { savedListRepository.deleteSavedListById(listId) } throws RuntimeException("Deletion failed")

        // When
        val result = runCatching { deleteSavedListUseCase(listId) }

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Deletion failed")

        coVerify(exactly = 1) { savedListRepository.deleteSavedListById(listId) }
    }
}