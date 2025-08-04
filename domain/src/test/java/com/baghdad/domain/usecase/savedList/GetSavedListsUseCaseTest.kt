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

    @Test
    fun `invoke should propagate exception when repository throws exception`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")

        coEvery {
            savedListRepository.getSavedLists(page, pageSize)
        } throws expectedException

        // When & Then
        val exception = runCatching {
            getSavedListsUseCase(page, pageSize)
        }.exceptionOrNull()

        assertThat(exception).isNotNull()
        assertThat(exception).isInstanceOf(RuntimeException::class.java)
        assertThat(exception?.message).isEqualTo("Network error")
        coVerify(exactly = 1) { savedListRepository.getSavedLists(page, pageSize) }
    }

    @Test
    fun `invoke should pass correct parameters to repository`() = runTest {
        // Given
        coEvery {
            savedListRepository.getSavedLists(3, 10)
        } returns PagedResult(
            data = emptyList(),
            nextKey = null,
            prevKey = 2
        )

        // When
        getSavedListsUseCase(3, 10)

        // Then
        coVerify(exactly = 1) { savedListRepository.getSavedLists(3, 10) }
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
