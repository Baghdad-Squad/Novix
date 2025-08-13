package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getMinimalSavedMovie
import com.baghdad.domain.testHelper.getSampleMovie
import com.baghdad.domain.testHelper.getSampleSavedMovie
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieCategoryUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieCategoryUseCase: GetMovieCategoryUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieCategoryUseCase = GetMovieCategoryUseCase(movieRepository)
    }

    @Test
    fun `getMovieCategoryUseCase() should return multiple genres when movie has multiple categories`() =
        runTest {
            coEvery { movieRepository.getMovieDetails(MOVIE_ID_MULTIPLE_GENRES) } returns mixGenreMovie

            val result = getMovieCategoryUseCase(MOVIE_ID_MULTIPLE_GENRES)

            assertThat(result).hasSize(3)
        }

    @Test
    fun `getMovieCategoryUseCase() should return empty list when movie has no genres`() = runTest {
        coEvery { movieRepository.getMovieDetails(MOVIE_ID_NO_GENRES) } returns minimalMovie

        val result = getMovieCategoryUseCase(MOVIE_ID_NO_GENRES)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieCategoryUseCase() should return single genre when movie has one category`() =
        runTest {
            coEvery { movieRepository.getMovieDetails(MOVIE_ID_SINGLE_GENRE) } returns singleGenreMovie

            val result = getMovieCategoryUseCase(MOVIE_ID_SINGLE_GENRE)

            assertThat(result).hasSize(1)
            assertThat(result[0].name).isEqualTo("Drama")
        }

    @Test
    fun `getMovieCategoryUseCase() should return genres with special characters when present`() =
        runTest {
            val movieId = 4L

            coEvery { movieRepository.getMovieDetails(MOVIE_ID_SPECIAL_CHAR_GENRES) } returns specialGenreMovie

            val result = getMovieCategoryUseCase(MOVIE_ID_SPECIAL_CHAR_GENRES)

            assertThat(result[0].name).isEqualTo("Sci-Fi/Fantasy")
            assertThat(result[1].name).isEqualTo("Action-Adventure")
        }


    @Test
    fun `getMovieCategoryUseCase() should return different genres when called with different movie IDs`() =
        runTest {

            coEvery { movieRepository.getMovieDetails(MOVIE_ID_MULTIPLE_GENRES) } returns mixGenreMovie
            coEvery { movieRepository.getMovieDetails(MOVIE_ID_COMEDY) } returns comedyMovie

            val result1 = getMovieCategoryUseCase(MOVIE_ID_MULTIPLE_GENRES)
            val result2 = getMovieCategoryUseCase(MOVIE_ID_COMEDY)

            assertThat(result1).isNotEqualTo(result2)
            assertThat(result1[0].name).isEqualTo("Sci-Fi")
            assertThat(result2[0].name).isEqualTo("Comedy")
        }

    companion object{
        private val sampleMovie = getSampleSavedMovie()

        private val mixGenreMovie = sampleMovie.copy(
            movie = sampleMovie.movie.copy(
                genres = listOf(
                    Genre(id = 1L, name = "Sci-Fi"),
                    Genre(id = 2L, name = "Action"),
                    Genre(id = 3L, name = "Thriller")
                )
            )
        )

        private val minimalMovie = getMinimalSavedMovie()

        private val singleGenreMovie = getSampleSavedMovie().copy(
            movie = getSampleMovie(
                genres = listOf(Genre(id = 4L, name = "Drama"))
            )
        )

        val comedyMovie = sampleMovie.copy(
            movie = sampleMovie.movie.copy(
                id = MOVIE_ID_COMEDY,
                genres = listOf(Genre(id = 8L, name = "Comedy"))
            )
        )

        val specialGenreMovie = getSampleSavedMovie().copy(
            movie = getSampleMovie(
                genres = listOf(
                    Genre(id = 5L, name = "Sci-Fi/Fantasy"),
                    Genre(id = 6L, name = "Action-Adventure")
                )
            )
        )

        const val MOVIE_ID_MULTIPLE_GENRES = 1L
        const val MOVIE_ID_NO_GENRES = 2L
        const val MOVIE_ID_SINGLE_GENRE = 3L
        const val MOVIE_ID_SPECIAL_CHAR_GENRES = 4L
        const val MOVIE_ID_UNICODE_GENRES = 5L
        const val MOVIE_ID_COMEDY = 6L
        const val MOVIE_ID_ORDERED_GENRES = 7L
    }
}
