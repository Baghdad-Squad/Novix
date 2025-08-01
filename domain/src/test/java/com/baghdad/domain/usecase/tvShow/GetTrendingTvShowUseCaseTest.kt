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

class GetTrendingTvShowUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var useCase: GetTrendingTvShowUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk()
        useCase = GetTrendingTvShowUseCase(tvShowRepository)
    }

    @Test
    fun `getTrendingTvShows() should return all tv shows with pagination when genreId is null`() = runTest {
        // Given
        val page = 1
        val shows = listOf(
            createTvShow(1, listOf(Genre(1, "Drama"))),
            createTvShow(2, listOf(Genre(2, "Comedy")))
        )
        val expectedResult = PagedResult(
            data = shows,
            nextKey = 2,
            prevKey = null
        )

        coEvery { tvShowRepository.getTrendingTvShows(page) } returns expectedResult

        // When
        val result = useCase(page, genreId = null)

        // Then
        assertThat(result.data).containsExactlyElementsIn(shows)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowRepository.getTrendingTvShows(page) }
    }

    @Test
    fun `getTrendingTvShows() should return filtered tv shows when genreId is provided`() = runTest {
        // Given
        val page = 1
        val genreId = 1L
        val shows = listOf(
            createTvShow(1, listOf(Genre(1, "Drama"))),
            createTvShow(2, listOf(Genre(2, "Comedy"))),
            createTvShow(3, listOf(Genre(1, "Drama"), Genre(3, "Action")))
        )

        val inputResult = PagedResult(
            data = shows,
            nextKey = 2,
            prevKey = null
        )

        coEvery { tvShowRepository.getTrendingTvShows(page) } returns inputResult

        // When
        val result = useCase(page, genreId)

        // Then
        val expectedShows = listOf(shows[0], shows[2])
        assertThat(result.data).containsExactlyElementsIn(expectedShows)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowRepository.getTrendingTvShows(page) }
    }

    @Test
    fun `getTrendingTvShows() should return empty list when no tv shows match genreId`() = runTest {
        // Given
        val page = 1
        val genreId = 99L
        val shows = listOf(
            createTvShow(1, listOf(Genre(1, "Drama"))),
            createTvShow(2, listOf(Genre(2, "Comedy")))
        )

        val inputResult = PagedResult(
            data = shows,
            nextKey = 2,
            prevKey = null
        )

        coEvery { tvShowRepository.getTrendingTvShows(page) } returns inputResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
        coVerify(exactly = 1) { tvShowRepository.getTrendingTvShows(page) }
    }

    @Test
    fun `getTrendingTvShows() should preserve pagination keys when filtering by genreId`() = runTest {
        // Given
        val page = 2
        val genreId = 1L
        val shows = listOf(
            createTvShow(1, listOf(Genre(1, "Drama"))),
            createTvShow(2, listOf(Genre(2, "Comedy")))
        )

        val inputResult = PagedResult(
            data = shows,
            nextKey = 3,
            prevKey = 1
        )

        coEvery { tvShowRepository.getTrendingTvShows(page) } returns inputResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
        coVerify(exactly = 1) { tvShowRepository.getTrendingTvShows(page) }
    }

    private fun createTvShow(id: Long, genres: List<Genre>): TvShow {
        return TvShow(
            id = id,
            title = "TV Show $id",
            genres = genres,
            averageRating = 8.5,
            userRating = null,
            releaseDate = LocalDate.parse("2022-01-01"),
            overview = "Overview of show $id",
            posterImageURL = "poster$id.jpg",
            trailerURL = "trailer$id.mp4",
            headerImagesURLs = listOf("header$id.jpg"),
            numberOfSeasons = 2
        )
    }
}