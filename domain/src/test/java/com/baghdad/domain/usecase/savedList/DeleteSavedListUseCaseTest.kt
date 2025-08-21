package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteSavedListUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val deleteSavedListUseCase = DeleteSavedListUseCase(savedListRepository)

    @Test
    fun `deleteSavedListUseCase should delete the list when selected`() = runTest {
        coEvery { savedListRepository.deleteSavedListById(any()) } just Runs

        deleteSavedListUseCase.invoke(listId)

        coVerify(exactly = 1) { savedListRepository.deleteSavedListById(listId) }
    }

    private companion object {
        val listId = 1L
    }
}