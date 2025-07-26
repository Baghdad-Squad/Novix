package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowsByGenreUseCaseTest {

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowsByGenreUseCase = GetTvShowsByGenreUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowsByGenre returns list of tv shows for given genre`() = runTest {
        // Given
        val genreId = 18L // Drama
        val page = 1
        val expectedShows = listOf(
            sampleTvShow.copy(id = 1, title = "Breaking Bad"),
            sampleTvShow.copy(id = 2, title = "The Sopranos")
        )
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, page) } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase(genreId, page)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.title }).containsExactly("Breaking Bad", "The Sopranos")
    }

    @Test
    fun `getTvShowsByGenre returns empty list when no shows found`() = runTest {
        // Given
        val genreId = 99L // Non-existent genre
        val page = 1
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, page) } returns emptyList()

        // When
        val result = getTvShowsByGenreUseCase(genreId, page)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowsByGenre makes exactly one repository call`() = runTest {
        // Given
        val genreId = 35L // Comedy
        val page = 2
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, page) } returns emptyList()

        // When
        getTvShowsByGenreUseCase(genreId, page)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowsByGenre(genreId, page) }
    }

    @Test
    fun `getTvShowsByGenre returns different results for different pages`() = runTest {
        // Given
        val genreId = 10759L // Action & Adventure
        val page1Shows = listOf(sampleTvShow.copy(id = 1), sampleTvShow.copy(id = 2))
        val page2Shows = listOf(sampleTvShow.copy(id = 3), sampleTvShow.copy(id = 4))
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 1) } returns page1Shows
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 2) } returns page2Shows

        // When
        val resultPage1 = getTvShowsByGenreUseCase(genreId, 1)
        val resultPage2 = getTvShowsByGenreUseCase(genreId, 2)

        // Then
        assertThat(resultPage1.map { it.id }).containsExactly(1L, 2L)
        assertThat(resultPage2.map { it.id }).containsExactly(3L, 4L)
    }

    @Test
    fun `getTvShowsByGenre returns different results for different genres`() = runTest {
        // Given
        val dramaShows = listOf(sampleTvShow.copy(id = 1, title = "The Crown"))
        val comedyShows = listOf(sampleTvShow.copy(id = 2, title = "The Office"))
        coEvery { tvShowRepository.getTvShowsByGenre(18L, 1) } returns dramaShows // Drama
        coEvery { tvShowRepository.getTvShowsByGenre(35L, 1) } returns comedyShows // Comedy

        // When
        val dramaResult = getTvShowsByGenreUseCase(18L, 1)
        val comedyResult = getTvShowsByGenreUseCase(35L, 1)

        // Then
        assertThat(dramaResult[0].title).isEqualTo("The Crown")
        assertThat(comedyResult[0].title).isEqualTo("The Office")
    }

    @Test
    fun `getTvShowsByGenre handles large result sets`() = runTest {
        // Given
        val genreId = 10765L // Sci-Fi & Fantasy
        val page = 1
        val largeList = List(50) { index ->
            sampleTvShow.copy(id = index.toLong(), title = "Show $index")
        }
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, page) } returns largeList

        // When
        val result = getTvShowsByGenreUseCase(genreId, page)

        // Then
        assertThat(result).hasSize(50)
        assertThat(result.last().title).isEqualTo("Show 49")
    }

    companion object {
        private lateinit var tvShowRepository: TvShowRepository
        private lateinit var getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase

        private val sampleTvShow = TvShow(
            id = 1L,
            title = "Sample Show",
            posterImageURL = "/sample.jpg",
            overview = "Sample overview",
            genres = listOf(
                Genre(id = 18L, name = "Drama"),
            ),
            averageRating = 9.5,
            userRating = 8.5,
            releaseDate = LocalDate(2020, 1, 1),
            trailerURL = "sample_trailer.mp4",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
            numberOfSeasons = 5,
        )
    }
}