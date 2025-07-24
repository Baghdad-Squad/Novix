package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodeDetailsUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(episodeRepository)
    }

    @Test
    fun `getEpisodeDetailsUseCase returns episode details when episode exists`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 1
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns sampleEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(sampleEpisode)
    }

    @Test
    fun `getEpisodeDetailsUseCase returns episode with minimal information`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 2
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns minimalEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.title).isEqualTo("Minimal Episode")
        assertThat(result.overview).isEmpty()
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `getEpisodeDetailsUseCase returns episode with multiple genres`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 3
        val episodeWithGenres = sampleEpisode.copy(
            genres = listOf(
                Genre(id = 4L, name = "Thriller"),
                Genre(id = 5L, name = "Mystery")
            )
        )
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns episodeWithGenres

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.genres).hasSize(2)
    }

    @Test
    fun `getEpisodeDetailsUseCase returns episode with multiple header images`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 4
        val episodeWithHeaders = sampleEpisode.copy(
            headerPictures = List(5) { "https://example.com/header$it.jpg" }
        )
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns episodeWithHeaders

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.headerPictures).hasSize(5)
    }

    @Test
    fun `getEpisodeDetailsUseCase makes exactly one repository call`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 6
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns sampleEpisode

        // When
        getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `getEpisodeDetailsUseCase returns episode with special characters in title`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 7
        val specialTitleEpisode = sampleEpisode.copy(
            title = "Épisode Spécial: 特別編"
        )
        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns specialTitleEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result.title).isEqualTo("Épisode Spécial: 特別編")
    }

    companion object{
        private val sampleEpisode = Episode(
            id = 1L,
            title = "Pilot",
            overview = "The first episode of the series.",
            episodeNumber = 1,
            rating = 8.5,
            duration = "160",
            releasedDate = LocalDate(2025, 7, 23),
            trailerUrl = "https://example.com/trailer.mp4",
            currentSeason = 1,
            genres = listOf(
                Genre(id = 1L, name = "Drama"),
                Genre(id = 2L, name = "Action"),
                Genre(id = 3L, name = "Comedy")
            ),
            headerPictures = listOf(
                "https://example.com/header1.jpg",
                "https://example.com/header2.jpg"
            )
        )

        private val minimalEpisode = Episode(
            id = 2L,
            title = "Minimal Episode",
            overview = "",
            episodeNumber = 2,
            rating = 0.0,
            duration = "",
            releasedDate = LocalDate(2025, 7, 24),
            trailerUrl = "",
            currentSeason = 1,
            genres = emptyList(),
            headerPictures = emptyList()
        )

    }
}