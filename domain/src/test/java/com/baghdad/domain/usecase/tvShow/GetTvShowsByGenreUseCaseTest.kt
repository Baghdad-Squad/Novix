package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.pagination.PagedResult
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

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowsByGenreUseCase = GetTvShowsByGenreUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowsByGenreUseCase() should return list of tv shows when repository returns data for given genre`() = runTest {
        // Given
        val genreId = 18L
        val page = 1
        val expectedShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextPage = 2,
            prevPage = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase(genreId, page, PAGE_SIZE)

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data.map { it.title }).containsExactly("Breaking Bad", "The Sopranos")
    }

    @Test
    fun `getTvShowsByGenreUseCase() should return empty list when repository returns no shows for given genre`() = runTest {
        // Given
        val genreId = 99L
        val page = 1
        val expectedShows = PagedResult(
            data = emptyList<TvShow>(),
            nextPage = 2,
            prevPage = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase(genreId, page, PAGE_SIZE)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `getTvShowsByGenreUseCase() should make exactly one repository call per invocation`() = runTest {
        // Given
        val genreId = 35L
        val page = 2
        val expectedShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextPage = 2,
            prevPage = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        getTvShowsByGenreUseCase(genreId, page, PAGE_SIZE)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowsByGenre(genreId, page, pageSize = 20) }
    }

    @Test
    fun `getTvShowsByGenreUseCase() should return different results when called with different pages`() = runTest {
        // Given
        val genreId = 10759L
        val page1Shows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "Breaking Bad"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextPage = 2,
            prevPage = null
        )
        val page2Shows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 3, title = "The Crown"),
                sampleTvShow.copy(id = 4, title = "The Office")
            ),
            nextPage = 3,
            prevPage = 1
        )
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 1, pageSize = 20) } returns page1Shows
        coEvery { tvShowRepository.getTvShowsByGenre(genreId, 2, pageSize = 20) } returns page2Shows

        // When
        val resultPage1 = getTvShowsByGenreUseCase(genreId, 1, PAGE_SIZE)
        val resultPage2 = getTvShowsByGenreUseCase(genreId, 2, PAGE_SIZE)

        // Then
        assertThat(resultPage1.data.map { it.id }).containsExactly(1L, 2L)
        assertThat(resultPage2.data.map { it.id }).containsExactly(3L, 4L)
    }

    @Test
    fun `getTvShowsByGenreUseCase() should return different results when called with different genres`() = runTest {
        // Given
        val dramaShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 1, title = "The Crown"),
                sampleTvShow.copy(id = 2, title = "The Sopranos")
            ),
            nextPage = 2,
            prevPage = null
        )
        val comedyShows = PagedResult(
            data = listOf(
                sampleTvShow.copy(id = 3, title = "The Office"),
                sampleTvShow.copy(id = 4, title = "The Simpsons")
            ),
            nextPage = 2,
            prevPage = null
        )
        coEvery { tvShowRepository.getTvShowsByGenre(18L, 1, pageSize = 20) } returns dramaShows // Drama
        coEvery { tvShowRepository.getTvShowsByGenre(35L, 1, pageSize = 20) } returns comedyShows // Comedy

        // When
        val dramaResult = getTvShowsByGenreUseCase(18L, 1, PAGE_SIZE)
        val comedyResult = getTvShowsByGenreUseCase(35L, 1, PAGE_SIZE)

        // Then
        assertThat(dramaResult.data[0].title).isEqualTo("The Crown")
        assertThat(comedyResult.data[0].title).isEqualTo("The Office")
    }

    @Test
    fun `getTvShowsByGenreUseCase() should handle large result sets when repository returns many shows`() = runTest {
        // Given
        val genreId = 10765L
        val page = 1
        val largeList = List(50) { index ->
            sampleTvShow.copy(id = index.toLong(), title = "Show $index")
        }
        val expectedShows = PagedResult(
            data = largeList,
            nextPage = 2,
            prevPage = null
        )
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                genreId,
                page,
                pageSize = 20
            )
        } returns expectedShows

        // When
        val result = getTvShowsByGenreUseCase(genreId, page, PAGE_SIZE)

        // Then
        assertThat(result.data).hasSize(50)
        assertThat(result.data.last().title).isEqualTo("Show 49")
    }

    companion object {
        private val sampleTvShow = TvShow(
            id = 1L,
            title = "Sample Show",
            posterImageURL = "/sample.jpg",
            overview = "Sample overview",
            genres = listOf(Genre(id = 18L, name = "Drama")),
            averageRating = 9.5,
            userRating = 8,
            releaseDate = LocalDate(2020, 1, 1),
            trailerURL = "sample_trailer.mp4",
            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
            numberOfSeasons = 5,
        )
        private const val PAGE_SIZE = 20
    }
}