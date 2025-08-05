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
import org.junit.jupiter.api.assertThrows

class AddMovieToSavedListUseCaseTest {

    private lateinit var addMovieToSavedUseCase: AddMovieToSavedListUseCase
    private lateinit var savedListRepository: SavedListRepository
    private val listId = 22L
    private val movieId = 2002L

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk(relaxed = true)
        addMovieToSavedUseCase = AddMovieToSavedListUseCase(savedListRepository)
    }

    @Test
    fun `should return success response when adding a movie to saved list`() = runTest {
        coEvery { savedListRepository.addMovieToSavedList(listId, movieId) } just Runs

        addMovieToSavedUseCase(listId, movieId)

        coVerify { savedListRepository.addMovieToSavedList(listId, movieId) }
    }

    @Test
    fun `should throw exception when api returns error while adding a movie to saved list`() =
        runTest {
            coEvery { savedListRepository.addMovieToSavedList(listId, movieId) } throws Exception()

            assertThrows<Exception> { addMovieToSavedUseCase(listId, movieId) }

            coVerify { savedListRepository.addMovieToSavedList(listId, movieId) }
        }
}