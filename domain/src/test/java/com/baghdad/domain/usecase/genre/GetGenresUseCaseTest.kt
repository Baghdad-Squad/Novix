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
    fun `should return combined movies and tv shows genres when both are distinct`() = runTest {
        val movieGenres = listOf(actionGenre, comedyGenre)
        val tvShowGenres = listOf(dramaGenre, thrillerGenre)
        coEvery { movieRepository.getGenres() } returns movieGenres
        coEvery { tvShowRepository.getGenres() } returns tvShowGenres

        val result = getGenresUseCase()

        assertThat(result).containsExactly(actionGenre, comedyGenre, dramaGenre, thrillerGenre)
    }

    @Test
    fun `should return distinct genres when both repositories have duplicate genres`() = runTest {
        val movieGenres = listOf(actionGenre, comedyGenre)
        val tvShowGenres = listOf(actionGenre, dramaGenre)
        coEvery { movieRepository.getGenres() } returns movieGenres
        coEvery { tvShowRepository.getGenres() } returns tvShowGenres

        val result = getGenresUseCase()

        assertThat(result).containsExactly(
            actionGenre,
            comedyGenre,
            dramaGenre
        )
    }

    @Test
    fun `should return empty list when both repositories return empty lists`() = runTest {
        coEvery { movieRepository.getGenres() } returns emptyList()
        coEvery { tvShowRepository.getGenres() } returns emptyList()

        val result = getGenresUseCase()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return only movie genres when tv show repository returns empty list`() = runTest {
        val movieGenres = listOf(actionGenre, comedyGenre)
        coEvery { movieRepository.getGenres() } returns movieGenres
        coEvery { tvShowRepository.getGenres() } returns emptyList()

        val result = getGenresUseCase()

        assertThat(result).containsExactly(actionGenre, comedyGenre)
    }

    @Test
    fun `should return only tv show genres when movie repository returns empty list`() = runTest {
        val tvShowGenres = listOf(dramaGenre, thrillerGenre)
        coEvery { movieRepository.getGenres() } returns emptyList()
        coEvery { tvShowRepository.getGenres() } returns tvShowGenres

        val result = getGenresUseCase()

        assertThat(result).containsExactly(dramaGenre, thrillerGenre)
    }

    @Test
    fun `should propagate exception when repository throws exception`() = runTest {
        val exception = RuntimeException()
        coEvery { movieRepository.getGenres() } throws exception
        coEvery { tvShowRepository.getGenres() } returns emptyList()

        val resultException = runCatching { getGenresUseCase() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}