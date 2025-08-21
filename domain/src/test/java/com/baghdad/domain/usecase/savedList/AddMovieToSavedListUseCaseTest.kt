package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddMovieToSavedListUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val addMovieToSavedUseCase = AddMovieToSavedListUseCase(savedListRepository)

    @Test
    fun `addMovieToSavedUseCase should return success response when adding a movie to saved list`() =
        runTest {
            coEvery { savedListRepository.addMovieToSavedList(listId, movieId) } just Runs

            addMovieToSavedUseCase(listId, movieId)

            coVerify { savedListRepository.addMovieToSavedList(listId, movieId) }
        }

    private companion object {
        val listId = 22L
        val movieId = 2002L
    }
}