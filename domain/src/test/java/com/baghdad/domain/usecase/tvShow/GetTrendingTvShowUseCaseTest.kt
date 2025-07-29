package com.baghdad.domain.usecase.tvShow

import com.baghdad.entity.media.Genre
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
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
    fun `invoke returns all trending tv shows when genreId is null`() = runTest {
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
        assertEquals(expectedResult, result)
        coVerify { tvShowRepository.getTrendingTvShows(page) }
    }

    @Test
    fun `invoke returns filtered tv shows by genreId when genreId is provided`() = runTest {
        // Given
        val page = 1
        val genreId: Long = 1

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
        val expected = listOf(shows[0], shows[2])
        assertEquals(expected, result.data)
        assertEquals(2, result.nextKey)
        assertEquals(null, result.prevKey)
        coVerify { tvShowRepository.getTrendingTvShows(page) }
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
