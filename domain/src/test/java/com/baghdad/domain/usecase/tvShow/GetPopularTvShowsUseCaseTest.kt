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

class GetPopularTvShowsUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var useCase: GetPopularTvShowsUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk()
        useCase = GetPopularTvShowsUseCase(tvShowRepository)
    }

    private fun createTvShow(id: Long): TvShow {
        return TvShow(
            id = id,
            title = "TV Show $id",
            genres = listOf(Genre(1, "Drama")),
            averageRating = 8.2,
            userRating = null,
            releaseDate = LocalDate.parse("2023-05-01"),
            overview = "Popular TV show overview",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            headerImagesURLs = listOf("header1.jpg", "header2.jpg"),
            numberOfSeasons = 3
        )
    }

    @Test
    fun `getPopularTvShowsUseCase() should return popular tv shows when repository returns data`() = runTest {
        // Given
        val expected = listOf(createTvShow(1), createTvShow(2))
        coEvery { tvShowRepository.getPopularTvShows() } returns expected

        // When
        val result = useCase()

        // Then
        assertThat(result).isEqualTo(expected)
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShowsUseCase() should return empty list when repository returns empty list`() = runTest {
        // Given
        val expected = emptyList<TvShow>()
        coEvery { tvShowRepository.getPopularTvShows() } returns expected

        // When
        val result = useCase()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }
}