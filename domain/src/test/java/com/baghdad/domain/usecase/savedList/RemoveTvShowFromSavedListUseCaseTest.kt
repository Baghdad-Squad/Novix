package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveTvShowFromSavedListUseCaseTest {

    private lateinit var removeTvShowFromSavedListUseCase: RemoveTvShowFromSavedListUseCase
    private lateinit var savedListRepository: SavedListRepository

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk()
        removeTvShowFromSavedListUseCase = RemoveTvShowFromSavedListUseCase(savedListRepository)
    }

    @Test
    fun `should remove tvShow from saved list when the tvShow removed successfully`() = runTest {
        // Given
        val listId = 1L
        val tvShowId = 2L
        coEvery { savedListRepository.removeTvShowFromSavedList(listId, tvShowId) } returns Unit

        // When
        removeTvShowFromSavedListUseCase(listId, tvShowId)

        // Then
        coVerify { savedListRepository.removeTvShowFromSavedList(listId, tvShowId) }
    }
}