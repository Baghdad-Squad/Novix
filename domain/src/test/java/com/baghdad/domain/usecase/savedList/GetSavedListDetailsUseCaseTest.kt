package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetSavedListDetailsUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val getSavedListDetailsUseCase = GetSavedListDetailsUseCase(savedListRepository)

    @Test
    fun `should return SavedListDetails when repository returns data successfully`() =
        runTest {
            coEvery {
                savedListRepository.getSavedListDetails(listId, page, pageSize)
            } returns savedListDetails

            val result = getSavedListDetailsUseCase(listId, page, pageSize)

            assertThat(result).isEqualTo(savedListDetails)
        }

    private companion object {
        val savedListDetails = SavedListMock.SAVED_LIST_DETAILS
        val page = 1
        val pageSize = 20
        val listId = 1L
    }
}
