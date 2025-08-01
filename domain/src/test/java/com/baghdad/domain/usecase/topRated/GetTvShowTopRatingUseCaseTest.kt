package com.baghdad.domain.usecase.topRated

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

class GetTvShowTopRatingUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var useCase: GetTvShowTopRatingUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk()
        useCase = GetTvShowTopRatingUseCase(tvShowRepository)
    }

    private fun createTvShow(id: Long, genreIds: List<Long>): TvShow {
        val genres = genreIds.map { Genre(it, "Genre $it") }
        return TvShow(
            id = id,
            title = "TV Show $id",
            genres = genres,
            averageRating = 9.0,
            userRating = null,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Overview for TV Show $id",
            posterImageURL = "https://example.com/tv$id.jpg",
            trailerURL = "https://example.com/tv$id.mp4",
            headerImagesURLs = listOf("https://example.com/tv$id-header1.jpg"),
            numberOfSeasons = 3
        )
    }

    @Test
    fun `getTopRatedTvShows() should return all tv shows when genreId is null`() = runTest {
        // Given
        val page = 1
        val tvShows = listOf(
            createTvShow(1, listOf(1)),
            createTvShow(2, listOf(2))
        )
        val pagedResult = PagedResult(data = tvShows, nextKey = 2, prevKey = null)

        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns pagedResult

        // When
        val result = useCase(page, genreId = null)

        // Then
        assertThat(result.data).containsExactlyElementsIn(tvShows)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page) }
    }

    @Test
    fun `getTopRatedTvShows() should return filtered tv shows when genreId is provided`() = runTest {
        // Given
        val page = 1
        val genreId = 2L
        val tvShows = listOf(
            createTvShow(1, listOf(1)),
            createTvShow(2, listOf(2)),
            createTvShow(3, listOf(2, 3))
        )
        val expected = listOf(tvShows[1], tvShows[2])
        val pagedResult = PagedResult(data = tvShows, nextKey = 2, prevKey = null)

        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns pagedResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertThat(result.data).containsExactlyElementsIn(expected)
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page) }
    }

    @Test
    fun `getTopRatedTvShows() should return empty list when no tv shows match genreId`() = runTest {
        // Given
        val page = 1
        val genreId = 99L
        val tvShows = listOf(
            createTvShow(1, listOf(1)),
            createTvShow(2, listOf(2))
        )
        val pagedResult = PagedResult(data = tvShows, nextKey = 2, prevKey = null)

        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns pagedResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertThat(result.data).isEmpty()
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page) }
    }

    @Test
    fun `getTopRatedTvShows() should preserve pagination keys when filtering by genreId`() = runTest {
        // Given
        val page = 2
        val genreId = 2L
        val tvShows = listOf(
            createTvShow(1, listOf(1)),
            createTvShow(2, listOf(2))
        )
        val pagedResult = PagedResult(
            data = tvShows,
            nextKey = 3,
            prevKey = 1
        )

        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns pagedResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
        assertThat(result.data).hasSize(1)
    }
}