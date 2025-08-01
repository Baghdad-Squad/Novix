package com.baghdad.domain.usecase.genre

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetGenresUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getGenresUseCase: GetGenresUseCase

    private val actionGenre = Genre(id = 1, name = "Action")
    private val comedyGenre = Genre(id = 2, name = "Comedy")
    private val dramaGenre = Genre(id = 3, name = "Drama")
    private val thrillerGenre = Genre(id = 4, name = "Thriller")

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        tvShowRepository = mockk(relaxed = true)
        getGenresUseCase = GetGenresUseCase(movieRepository, tvShowRepository)
    }

    @Test
    fun `getMovieGenres() should return genres from movie repository`() = runTest {
        val movieGenres = listOf(actionGenre, comedyGenre)
        coEvery { movieRepository.getGenres() } returns movieGenres

        val result = getGenresUseCase.getMovieGenres()

        assertThat(result).containsExactly(actionGenre, comedyGenre)
    }

    @Test
    fun `getTvShowGenres() should return genres from tv show repository`() = runTest {
        val tvShowGenres = listOf(dramaGenre, thrillerGenre)
        coEvery { tvShowRepository.getGenres() } returns tvShowGenres

        val result = getGenresUseCase.getTvShowGenres()

        assertThat(result).containsExactly(dramaGenre, thrillerGenre)
    }

    @Test
    fun `getMovieGenres() should return empty list when movie repository returns empty list`() =
        runTest {
        coEvery { movieRepository.getGenres() } returns emptyList()

            val result = getGenresUseCase.getMovieGenres()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowGenres() should return empty list when tv show repository returns empty list`() =
        runTest {
        coEvery { tvShowRepository.getGenres() } returns emptyList()

            val result = getGenresUseCase.getTvShowGenres()

            assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieGenres() should propagate exception when movie repository throws exception`() =
        runTest {
            val exception = RuntimeException()
            coEvery { movieRepository.getGenres() } throws exception

            val resultException =
                runCatching { getGenresUseCase.getMovieGenres() }.exceptionOrNull()

            assertThat(resultException).isNotNull()
            assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `getTvShowGenres() should propagate exception when tv show repository throws exception`() =
        runTest {
        val exception = RuntimeException()
            coEvery { tvShowRepository.getGenres() } throws exception

            val resultException =
                runCatching { getGenresUseCase.getTvShowGenres() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}
