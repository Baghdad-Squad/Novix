package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.domain.usecase.movie.MovieMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class RemoveMovieFromSavedListUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val removeMovieFromSavedListUseCase =
        RemoveMovieFromSavedListUseCase(savedListRepository)

    @Test
    fun `removeMovieFromSavedListUseCase should remove movie from saved list when the movie removed successfully`() =
        runTest {
            coEvery { savedListRepository.removeMovieFromSavedList(listId, movieId) } returns Unit

            removeMovieFromSavedListUseCase(listId, movieId)

            coVerify { savedListRepository.removeMovieFromSavedList(listId, movieId) }
        }

    companion object {
        val listId = 1L
        val movieId = MovieMock.MOVIE_ID
    }
}