package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
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

class GetActorTvShowUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase

    private val sampleTvShows = listOf(
        TvShow(
            id = 1L,
            title = "Breaking Bad",
            overview = "A high school chemistry teacher turned meth maker",
            posterImageURL = "https://example.com/breakingbad.jpg",
            genres = listOf(
                Genre(id = 1, name = "Drama"), Genre(id = 2, name = "Crime")
            ),
            averageRating = 9.5,
            userRating = 9.4,
            releaseDate = LocalDate(2008, 1, 20),
            trailerURL = "https://example.com/breakingbad_trailer.mp4",
            headerImagesURLs = listOf(
                "https://example.com/breakingbad_header1.jpg",
                "https://example.com/breakingbad_header2.jpg"
            ),
            numberOfSeasons = 5
        ), TvShow(
            id = 2L,
            title = "Better Call Saul",
            overview = "The trials and tribulations of criminal lawyer Jimmy McGill",
            posterImageURL = "https://example.com/bettercallsaul.jpg",
            genres = listOf(
                Genre(id = 1, name = "Drama"), Genre(id = 3, name = "Legal")
            ),
            averageRating = 8.7,
            userRating = 8.9,
            releaseDate = LocalDate(2015, 2, 8),
            trailerURL = "https://example.com/bettercallsaul_trailer.mp4",
            headerImagesURLs = listOf("https://example.com/bettercallsaul_header.jpg"),
            numberOfSeasons = 6
        )
    )

    private val minimalTvShow = TvShow(
        id = 3L,
        title = "Minimal Show",
        overview = "",
        posterImageURL = "",
        genres = emptyList(),
        averageRating = 0.0,
        userRating = null,
        releaseDate = LocalDate(2021, 1, 1),
        trailerURL = "",
        headerImagesURLs = emptyList(),
        numberOfSeasons = 0
    )

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorTvShowUseCase = GetActorTvShowUseCase(actorRepository)
    }

    @Test
    fun `invoke returns tv shows when actor has appearances`() = runTest {
        // Given
        val actorId = 1L
        coEvery { actorRepository.getActorTvShows(actorId) } returns sampleTvShows

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result).isEqualTo(sampleTvShows)
        assertThat(result).hasSize(2)
    }

    @Test
    fun `invoke returns empty list when actor has no tv show appearances`() = runTest {
        // Given
        val actorId = 2L
        coEvery { actorRepository.getActorTvShows(actorId) } returns emptyList()

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke returns tv shows with minimal information`() = runTest {
        // Given
        val actorId = 3L
        coEvery { actorRepository.getActorTvShows(actorId) } returns listOf(minimalTvShow)

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Minimal Show")
        assertThat(result[0].posterImageURL).isEmpty()
        assertThat(result[0].genres).isEmpty()
        assertThat(result[0].headerImagesURLs).isEmpty()
    }

    @Test
    fun `invoke returns tv shows with multiple genres`() = runTest {
        // Given
        val actorId = 5L
        coEvery { actorRepository.getActorTvShows(actorId) } returns sampleTvShows

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result[0].genres).hasSize(2)
        assertThat(result[1].genres).hasSize(2)
    }

    @Test
    fun `invoke returns tv shows with header images`() = runTest {
        // Given
        val actorId = 6L
        coEvery { actorRepository.getActorTvShows(actorId) } returns sampleTvShows

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result[0].headerImagesURLs).hasSize(2)
        assertThat(result[1].headerImagesURLs).hasSize(1)
    }

    @Test
    fun `invoke returns tv shows with different season counts`() = runTest {
        // Given
        val actorId = 7L
        coEvery { actorRepository.getActorTvShows(actorId) } returns sampleTvShows

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result[0].numberOfSeasons).isEqualTo(5)
        assertThat(result[1].numberOfSeasons).isEqualTo(6)
    }

    @Test
    fun `invoke makes exactly one repository call`() = runTest {
        // Given
        val actorId = 8L
        coEvery { actorRepository.getActorTvShows(actorId) } returns sampleTvShows

        // When
        getActorTvShowUseCase(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorTvShows(actorId) }
    }

    @Test
    fun `invoke returns tv shows with special characters in titles`() = runTest {
        // Given
        val actorId = 9L
        val specialTitleShow = listOf(
            sampleTvShows[0].copy(title = "Stranger Things: ストレンジャー・シングス")
        )
        coEvery { actorRepository.getActorTvShows(actorId) } returns specialTitleShow

        // When
        val result = getActorTvShowUseCase(actorId)

        // Then
        assertThat(result[0].title).isEqualTo("Stranger Things: ストレンジャー・シングス")
    }
}