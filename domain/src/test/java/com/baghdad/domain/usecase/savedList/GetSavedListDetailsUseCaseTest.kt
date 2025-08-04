package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.model.savedList.SavedListItem
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.entity.savedList.SavedList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSavedListDetailsUseCaseTest {
    private lateinit var savedListRepository: SavedListRepository
    private lateinit var getSavedListDetailsUseCase: GetSavedListDetailsUseCase

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        getSavedListDetailsUseCase = GetSavedListDetailsUseCase(savedListRepository)
    }

    @Test
    fun `should return SavedListDetails when repository returns data successfully`() =
        runTest {
            // Given
            val listId = 1L
            val expected =
                SavedListDetails(
                    savedList = SavedList(1L, "My List", 2),
                    listItems =
                        listOf(
                            SavedListItem(100L, SavedListItem.Type.MOVIE, "Movie 1", "poster1"),
                            SavedListItem(101L, SavedListItem.Type.TV_SHOW, "TV Show", "poster2"),
                        ),
                )
            coEvery { savedListRepository.getSavedListDetails(listId) } returns expected

            // When
            val result = getSavedListDetailsUseCase(listId)

            // Then
            assertThat(result).isEqualTo(expected)
            coVerify(exactly = 1) { savedListRepository.getSavedListDetails(listId) }
        }

    @Test
    fun `should propagate exception when repository throws an exception`() =
        runTest {
            // Given
            val listId = 42L
            val exception = UnKnownNetworkException()
            coEvery { savedListRepository.getSavedListDetails(listId) } throws exception

            // When
            val thrown = runCatching { getSavedListDetailsUseCase(listId) }.exceptionOrNull()

            // Then
            assertThat(thrown).isInstanceOf(UnKnownNetworkException::class.java)
            coVerify(exactly = 1) { savedListRepository.getSavedListDetails(listId) }
        }
}
