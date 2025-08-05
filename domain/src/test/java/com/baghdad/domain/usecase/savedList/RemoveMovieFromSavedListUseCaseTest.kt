package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveMovieFromSavedListUseCaseTest {

    private lateinit var removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
    private lateinit var savedListRepository: SavedListRepository

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk()
        removeMovieFromSavedListUseCase = RemoveMovieFromSavedListUseCase(savedListRepository)
    }

    @Test
    fun `should remove movie from saved list when the movie removed successfully`() = runTest {
        // Given
        val listId = 1L
        val movieId = 2L
        coEvery { savedListRepository.removeMovieFromSavedList(listId, movieId) } returns Unit

        // When
        removeMovieFromSavedListUseCase(listId, movieId)

        // Then
        coVerify { savedListRepository.removeMovieFromSavedList(listId, movieId) }
    }
}