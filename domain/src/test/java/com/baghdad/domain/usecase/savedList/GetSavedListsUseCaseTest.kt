package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetSavedListsUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val getSavedListsUseCase = GetSavedListsUseCase(savedListRepository)

    @Test
    fun `getSavedListsUseCase should return paged result when repository returns data successfully`() =
        runTest {
            coEvery {
                savedListRepository.getSavedLists(page, pageSize)
            } returns savedListResult

            val result = getSavedListsUseCase(page, pageSize)

            assertThat(result).isEqualTo(savedListResult)
        }

    private companion object {
        val savedListResult = SavedListMock.SAVED_LIST_RESULT
        val page = 1
        val pageSize = 20
    }
}