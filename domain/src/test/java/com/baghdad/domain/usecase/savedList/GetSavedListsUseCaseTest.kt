package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSavedListsUseCaseTest {
    private lateinit var getSavedListsUseCase: GetSavedListsUseCase
    private lateinit var savedListRepository: SavedListRepository

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        getSavedListsUseCase = GetSavedListsUseCase(savedListRepository)
    }

    @Test
    fun `invoke should return paged result when repository returns data successfully`() = runTest {
        // Given
        coEvery {
            savedListRepository.getSavedLists(page, pageSize)
        } returns expectedPagedResultWithData

        // When
        val result = getSavedListsUseCase(page, pageSize)

        // Then
        assertThat(result).isEqualTo(expectedPagedResultWithData)
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].name).isEqualTo("My Favorites")
        assertThat(result.data[1].name).isEqualTo("Watch Later")
        coVerify(exactly = 1) { savedListRepository.getSavedLists(page, pageSize) }
    }

    @Test
    fun `invoke should return empty paged result when repository returns empty data`() = runTest {
        // Given
        coEvery {
            savedListRepository.getSavedLists(page, pageSize)
        } returns expectedPagedResultEmpty

        // When
        val result = getSavedListsUseCase(page, pageSize)

        // Then
        assertThat(result).isEqualTo(expectedPagedResultEmpty)
        assertThat(result.data).isEmpty()
        coVerify(exactly = 1) { savedListRepository.getSavedLists(page, pageSize) }
    }

    companion object {
        private const val page = 1
        private const val pageSize = 20

        private val expectedSavedLists = listOf(
            SavedList(id = 1L, name = "My Favorites", itemCount = 10),
            SavedList(id = 2L, name = "Watch Later", itemCount = 5)
        )

        private val expectedPagedResultWithData = PagedResult(
            data = expectedSavedLists,
            nextKey = 2,
            prevKey = null
        )

        private val expectedPagedResultEmpty = PagedResult<SavedList>(
            data = emptyList(),
            nextKey = null,
            prevKey = null
        )
    }
}
