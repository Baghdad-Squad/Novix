package com.baghdad.domain.usecase.genre

import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieGenreNameByIdUseCaseTest {

    private lateinit var getGenresUseCase: GetMovieGenresUseCase
    private lateinit var getMovieGenreNameByIdUseCase: GetMovieGenreNameByIdUseCase

    @BeforeEach
    fun setUp() {
        getGenresUseCase = mockk(relaxed = true)
        getMovieGenreNameByIdUseCase = GetMovieGenreNameByIdUseCase(getGenresUseCase)
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return correct genre for first ID in list`() = runTest {
        // Given
        val firstGenreId = 1L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        // When
        val result = getMovieGenreNameByIdUseCase(firstGenreId)

        // Then
        assertThat(result).isEqualTo(sampleGenres.first())
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return correct genre for last ID in list`() = runTest {
        val lastGenreId = 5L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        val result = getMovieGenreNameByIdUseCase(lastGenreId)

        assertThat(result).isEqualTo(sampleGenres.last())
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return correct genre for middle ID in list`() = runTest {
        val middleGenreId = 3L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        val result = getMovieGenreNameByIdUseCase(middleGenreId)

        assertThat(result).isEqualTo(sampleGenres[2])
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return correct genre when genres list is large`() = runTest {
        val largeGenresList = List(1000) { Genre(id = it.toLong(), name = "Genre $it") }
        val targetId = 999L
        coEvery { getGenresUseCase.getMovieGenres() } returns largeGenresList

        val result = getMovieGenreNameByIdUseCase(targetId)

        assertThat(result).isEqualTo(Genre(id = 999L, name = "Genre 999"))
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return same genre for multiple calls with same ID`() = runTest {
        val genreId = 2L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        val result1 = getMovieGenreNameByIdUseCase(genreId)
        val result2 = getMovieGenreNameByIdUseCase(genreId)

        assertThat(result1).isEqualTo(result2)
        assertThat(result1).isEqualTo(Genre(id = 2L, name = "Comedy"))
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return different genres for different IDs`() = runTest {
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        val actionGenre = getMovieGenreNameByIdUseCase(1L)
        val comedyGenre = getMovieGenreNameByIdUseCase(2L)

        assertThat(actionGenre).isNotEqualTo(comedyGenre)
        assertThat(actionGenre.name).isEqualTo("Action")
        assertThat(comedyGenre.name).isEqualTo("Comedy")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return genre with unicode characters`() = runTest {
        val genreId = 6L
        val unicodeGenre = Genre(id = 6L, name = "Dramédie Françaisé")
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres + unicodeGenre

        val result = getMovieGenreNameByIdUseCase(genreId)

        assertThat(result.name).isEqualTo("Dramédie Françaisé")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should return genre with punctuation in name`() = runTest {
        val genreId = 7L
        val punctuationGenre = Genre(id = 7L, name = "Sci-Fi/Fantasy (Action-Packed)")
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres + punctuationGenre

        val result = getMovieGenreNameByIdUseCase(genreId)

        assertThat(result.name).isEqualTo("Sci-Fi/Fantasy (Action-Packed)")
    }

    @Test
    fun `getMovieGenreNameByIdUseCase() should make only one call to genres repository per invocation`() = runTest {
        val genreId = 3L
        coEvery { getGenresUseCase.getMovieGenres() } returns sampleGenres

        getMovieGenreNameByIdUseCase(genreId)

        coVerify(exactly = 1) { getGenresUseCase.getMovieGenres() }
    }

    companion object {
        private val sampleGenres = listOf(
            Genre(id = 1L, name = "Action"),
            Genre(id = 2L, name = "Comedy"),
            Genre(id = 3L, name = "Drama"),
            Genre(id = 4L, name = "Science Fiction"),
            Genre(id = 5L, name = "Documentary")
        )
    }
}
