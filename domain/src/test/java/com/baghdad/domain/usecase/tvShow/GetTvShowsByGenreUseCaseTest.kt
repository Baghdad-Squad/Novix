package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.PagedResult
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
        val expectedShows = PagedResult(
            data = listOf(
            sampleTvShow.copy(id = 1, title = "Breaking Bad"),
            sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextKey = 2,
            prevKey = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase.invoke(genreId, page)

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data.map { it.title }).containsExactly("Breaking Bad", "The Sopranos")
    }

    @Test
    fun `getTvShowsByGenre returns empty list when no shows found`() = runTest {
        // Given
        val genreId = 99L // Non-existent genre
        val page = 1
        val expectedShows = PagedResult(
            data = emptyList<TvShow>(),
            nextKey = 2,
            prevKey = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase.invoke(genreId, page)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `getTvShowsByGenre makes exactly one repository call`() = runTest {
        // Given
        val genreId = 35L // Comedy
        val page = 2
        val expectedShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextKey = 2,
            prevKey = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        getTvShowsByGenreUseCase(genreId, page)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowsByGenre(genreId, page, pageSize = 20) }
    }

    @Test
    fun `getTvShowsByGenre returns different results for different pages`() = runTest {
        // Given
        val genreId = 10759L
        val expectedShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextKey = 2,
            prevKey = null
        )// Action & Adventure
        val page1Shows = expectedShows.copy(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            )
        )
        val page2Shows = expectedShows.copy(
            data = listOf(
                sampleTvShow.copy(id = 3, title = "The Crown"),
                sampleTvShow.copy(id = 4, title = "The Office")
            )
        )
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 1, pageSize = 20) } returns page1Shows
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 2, pageSize = 20) } returns page2Shows

        // When
        val resultPage1 = getTvShowsByGenreUseCase.invoke(genreId, 1)
        val resultPage2 = getTvShowsByGenreUseCase.invoke(genreId, 2)

        // Then
        assertThat(resultPage1.data.map { it.id }).containsExactly(1L, 2L)
        assertThat(resultPage2.data.map { it.id }).containsExactly(3L, 4L)
    }

    @Test
    fun `getTvShowsByGenre returns different results for different genres`() = runTest {
        // Given
        val dramaShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "The Crown"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextKey = 2,
            prevKey = null
        )
        val comedyShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 3, title = "The Office"),
                sampleTvShow.copy(id = 4, title = "The Simpsons")
            ),
            nextKey = 2,
            prevKey = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                18L,
                1,
                pageSize = 20
            )
        } returns dramaShows // Drama
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                35L,
                1,
                pageSize = 20
            )
        } returns comedyShows // Comedy

        // When
        val dramaResult = getTvShowsByGenreUseCase.invoke(18L, 1)
        val comedyResult = getTvShowsByGenreUseCase.invoke(35L, 1)

        // Then
        assertThat(dramaResult.data[0].title).isEqualTo("The Crown")
        assertThat(comedyResult.data[0].title).isEqualTo("The Office")
    }

    @Test
    fun `getTvShowsByGenre handles large result sets`() = runTest {
        // Given
        val genreId = 10765L // Sci-Fi & Fantasy
        val page = 1
        val largeList = List(50) { index ->
            sampleTvShow.copy(id = index.toLong(), title = "Show $index")
        }
        val expectedShows = PagedResult<TvShow>(
            data = largeList,
            nextKey = 2,
            prevKey = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase.invoke(genreId, page)

        // Then
        assertThat(result.data).hasSize(50)
        assertThat(result.data.last().title).isEqualTo("Show 49")
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