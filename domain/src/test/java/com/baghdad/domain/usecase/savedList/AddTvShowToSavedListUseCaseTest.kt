package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddTvShowToSavedListUseCaseTest {

    private lateinit var addTvShowToSavedListUseCase: AddTvShowToSavedListUseCase
    private lateinit var savedListRepository: SavedListRepository


    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        addTvShowToSavedListUseCase = AddTvShowToSavedListUseCase(savedListRepository)
    }

    @Test
    fun `should succeed when adding a tv show to saved list`() = runTest {
        // Given
        val listId = 22L
        val tvShowId = 2002L

        coEvery { savedListRepository.addTvShowToSavedList(listId, tvShowId) } just Runs

        // When
        addTvShowToSavedListUseCase(listId, tvShowId)

        // Then
        coVerify(exactly = 1) { savedListRepository.addTvShowToSavedList(listId, tvShowId) }
    }
}