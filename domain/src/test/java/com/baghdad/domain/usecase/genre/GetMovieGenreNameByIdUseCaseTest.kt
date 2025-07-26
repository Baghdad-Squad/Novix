package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieGenreNameByIdUseCaseTest {

    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase

    @BeforeEach
    fun setUp() {
        getGenresUseCase = mockk(relaxed = true)
        getMovieGenreNameByIdUseCase = GetMovieGenreNameByIdUseCase(getGenresUseCase)
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns correct genre for first ID in list`() = runTest {
        // Given
        val firstGenreId = 1L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val result = getMovieGenreNameByIdUseCase(firstGenreId)

        // Then
        assertThat(result).isEqualTo(sampleGenres.first())
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns correct genre for last ID in list`() = runTest {
        // Given
        val lastGenreId = 5L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val result = getMovieGenreNameByIdUseCase(lastGenreId)

        // Then
        assertThat(result).isEqualTo(sampleGenres.last())
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns correct genre for middle ID in list`() = runTest {
        // Given
        val middleGenreId = 3L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val result = getMovieGenreNameByIdUseCase(middleGenreId)

        // Then
        assertThat(result).isEqualTo(sampleGenres[2])
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns correct genre when genres list is large`() = runTest {
        // Given
        val largeGenresList = List(1000) { Genre(id = it.toLong(), name = "Genre $it") }
        val targetId = 999L
        coEvery { getGenresUseCase.getMovieGenres() } returns largeGenresList

        // When
        val result = getMovieGenreNameByIdUseCase(targetId)

        // Then
        assertThat(result).isEqualTo(Genre(id = 999L, name = "Genre 999"))
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns same genre for multiple calls with same ID`() = runTest {
        // Given
        val genreId = 2L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val result1 = getMovieGenreNameByIdUseCase(genreId)
        val result2 = getMovieGenreNameByIdUseCase(genreId)

        // Then
        assertThat(result1).isEqualTo(result2)
        assertThat(result1).isEqualTo(Genre(id = 2L, name = "Comedy"))
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns different genres for different IDs`() = runTest {
        // Given
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val actionGenre = getMovieGenreNameByIdUseCase(1L)
        val comedyGenre = getMovieGenreNameByIdUseCase(2L)

        // Then
        assertThat(actionGenre).isNotEqualTo(comedyGenre)
        assertThat(actionGenre.name).isEqualTo("Action")
        assertThat(comedyGenre.name).isEqualTo("Comedy")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns genre with unicode characters`() = runTest {
        // Given
        val genreId = 6L
        val unicodeGenre = Genre(id = 6L, name = "Dramédie Françaisé")
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres + unicodeGenre

        // When
        val result = getMovieGenreNameByIdUseCase(genreId)

        // Then
        assertThat(result.name).isEqualTo("Dramédie Françaisé")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase returns genre with punctuation in name`() = runTest {
        // Given
        val genreId = 7L
        val punctuationGenre = Genre(id = 7L, name = "Sci-Fi/Fantasy (Action-Packed)")
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres + punctuationGenre

        // When
        val result = getMovieGenreNameByIdUseCase(genreId)

        // Then
        assertThat(result.name).isEqualTo("Sci-Fi/Fantasy (Action-Packed)")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase makes only one call to genres repository per invocation`() = runTest {
        // Given
        val genreId = 3L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        getMovieGenreNameByIdUseCase(genreId)

        // Then
        coVerify(exactly = 1) { getGenresUseCase.getMovieGenres() }
    }

    companion object{
        private val sampleGenres = listOf(
            Genre(id = 1L, name = "Action"),
            Genre(id = 2L, name = "Comedy"),
            Genre(id = 3L, name = "Drama"),
            Genre(id = 4L, name = "Science Fiction"),
            Genre(id = 5L, name = "Documentary")
        )
    }
}

