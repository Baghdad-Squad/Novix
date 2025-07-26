package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowGenreNameByIdUseCaseTest {

    private lateinit var getGenresUseCase: GetGenresUseCase
    private lateinit var getTvShowGenreNameByIdUseCase: GetTvShowGenreNameByIdUseCase

    @BeforeEach
    fun setUp() {
        getGenresUseCase = mockk(relaxed = true)
        getTvShowGenreNameByIdUseCase = GetTvShowGenreNameByIdUseCase(getGenresUseCase)
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns correct genre for existing category ID`() = runTest {
        // Given
        val categoryId = 3L
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result).isEqualTo(Genre(id = 3L, name = "Sci-Fi & Fantasy"))
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns first match when duplicate IDs exist`() = runTest {
        // Given
        val categoryId = 2L
        val genresWithDuplicates = sampleTvGenres + Genre(id = 2L, name = "Sitcom")
        coEvery { getGenresUseCase.getTvShowGenres() } returns genresWithDuplicates

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result).isEqualTo(Genre(id = 2L, name = "Comedy"))
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns genre with special characters`() = runTest {
        // Given
        val categoryId = 6L
        val specialGenre = Genre(id = 6L, name = "Reality-TV/Docusoap")
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres + specialGenre

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result.name).isEqualTo("Reality-TV/Docusoap")
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns genre with long name`() = runTest {
        // Given
        val categoryId = 7L
        val longNameGenre = Genre(id = 7L, name = "Historical Fiction Drama Series")
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres + longNameGenre

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result.name.length).isGreaterThan(20)
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns correct genre from large genre list`() = runTest {
        // Given
        val largeGenreList = List(1000) { Genre(id = it.toLong(), name = "TV Genre $it") }
        val targetId = 999L
        coEvery { getGenresUseCase.getTvShowGenres() } returns largeGenreList

        // When
        val result = getTvShowGenreNameByIdUseCase(targetId)

        // Then
        assertThat(result).isEqualTo(Genre(id = 999L, name = "TV Genre 999"))
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase makes exactly one call to genres repository`() = runTest {
        // Given
        val categoryId = 1L
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres

        // When
        getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        coVerify(exactly = 1) { getGenresUseCase.getTvShowGenres() }
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns consistent results for same category ID`() = runTest {
        // Given
        val categoryId = 4L
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres

        // When
        val result1 = getTvShowGenreNameByIdUseCase(categoryId)
        val result2 = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result1).isEqualTo(result2)
        assertThat(result1.name).isEqualTo("Documentary")
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns different genres for different category IDs`() = runTest {
        // Given
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres

        // When
        val dramaGenre = getTvShowGenreNameByIdUseCase(1L)
        val crimeGenre = getTvShowGenreNameByIdUseCase(5L)

        // Then
        assertThat(dramaGenre).isNotEqualTo(crimeGenre)
        assertThat(dramaGenre.name).isEqualTo("Drama")
        assertThat(crimeGenre.name).isEqualTo("Crime")
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns genre with unicode characters`() = runTest {
        // Given
        val categoryId = 8L
        val unicodeGenre = Genre(id = 8L, name = "Télé-réalité")
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres + unicodeGenre

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result.name).isEqualTo("Télé-réalité")
    }

    @Test
    fun `getTvShowGenreNameByIdUseCase returns genre with numeric characters in name`() = runTest {
        // Given
        val categoryId = 9L
        val numericGenre = Genre(id = 9L, name = "24/7 Series")
        coEvery { getGenresUseCase.getTvShowGenres() } returns sampleTvGenres + numericGenre

        // When
        val result = getTvShowGenreNameByIdUseCase(categoryId)

        // Then
        assertThat(result.name).isEqualTo("24/7 Series")
    }

    companion object{
        private val sampleTvGenres = listOf(
            Genre(id = 1L, name = "Drama"),
            Genre(id = 2L, name = "Comedy"),
            Genre(id = 3L, name = "Sci-Fi & Fantasy"),
            Genre(id = 4L, name = "Documentary"),
            Genre(id = 5L, name = "Crime")
        )

    }
}