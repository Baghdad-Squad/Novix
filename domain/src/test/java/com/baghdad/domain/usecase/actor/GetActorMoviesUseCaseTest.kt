package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.usecase.movie.MovieMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetActorMoviesUseCaseTest {

    private val actorRepository = mockk<ActorRepository>()
    private val getActorMoviesUseCase = GetActorMoviesUseCase(actorRepository)

    @Test
    fun `getActorMoviesUseCase should return actor movies when called with valid actorId`() =
        runTest {
            coEvery { actorRepository.getActorMovies(actorId) } returns savedMovie

            val result = getActorMoviesUseCase(actorId)

            assertThat(result).isEqualTo(savedMovie)
        }

    @Test
    fun `getActorMoviesUseCase should return empty list when no movies found for actor`() =
        runTest {
            coEvery { actorRepository.getActorMovies(actorId) } returns emptyList()

            val result = getActorMoviesUseCase(actorId)

            assertThat(result).isEmpty()
        }

    companion object {
        private val actorId = ActorMock.ACTOR_ID
        val savedMovie = MovieMock.SAVED_MOVIES
    }
}