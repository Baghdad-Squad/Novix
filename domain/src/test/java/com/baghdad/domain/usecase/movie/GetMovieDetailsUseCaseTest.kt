package com.baghdad.domain.usecase.movie

import com.baghdad.domain.exception.NetworkException
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMovieDetailsUseCaseTest {
    lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setup() {
        movieRepository = mockk()
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)
    }

    @Test
    fun `should get movies detail from when function run successfully`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns movieDetail
        val result = getMovieDetailsUseCase.invoke(movieId)
        assertThat(result).isEqualTo(movieDetail)
    }

    @Test
    fun `throws exception when repository fails`() = runTest {
        val movieId = 2L
        coEvery { movieRepository.getMovieDetails(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            getMovieDetailsUseCase.invoke(movieId)
        }
    }


    private companion object {
        val movieDetail = Movie(
            id = 3L,
            title = "Interstellar",
            genres = listOf(Genre(12, "Adventure"), Genre(18, "Drama"), Genre(878, "Sci-Fi")),
            averageRating = 8.6,
            userRating = 9.2,
            releaseDate = LocalDate(2014, 11, 7),
            overview = "A group of explorers travel through a wormhole to save humanity.",
            posterImageURL = "https://example.com/posters/interstellar.jpg",
            runtimeMinutes = 169
        )
    }

}