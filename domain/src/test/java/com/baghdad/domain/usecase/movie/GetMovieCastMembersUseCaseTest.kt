package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.usecase.actor.ActorMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieCastMembersUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieCastMembersUseCase = GetMovieCastMembersUseCase(movieRepository)

    @Test
    fun `getMovieCastMembersUseCase should return cast members when repository returns data`() =
        runTest {
            coEvery { movieRepository.getMovieCastMembers(movieId = movieId) } returns CAST_MEMBERS

            val result = getMovieCastMembersUseCase.invoke(movieId = movieId)

            assertThat(result).isEqualTo(CAST_MEMBERS)
        }

    private companion object {
        val movieId = MovieMock.MOVIE.id
        val CAST_MEMBERS = ActorMock.CAST_MEMBERS
    }
}