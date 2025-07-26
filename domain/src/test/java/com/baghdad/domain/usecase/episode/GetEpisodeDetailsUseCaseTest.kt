package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class GetEpisodeDetailsUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase

    private val sampleEpisode = Episode(
        id = 123L,
        title = "The Heirs of the Dragon",
        episodeNumber = 1,
        rating = 8.5,
        duration = "1h 5m",
        releasedDate = LocalDate(2022, 8, 21),
        trailerUrl = "https://www.youtube.com/watch?v=example",
        currentSeason = 1,
        overview = "King Viserys hosts a tournament to celebrate the birth of his second child.",
        genres = listOf(Genre(1L, "Drama"), Genre(2L, "Fantasy")),
        headerPictures = listOf(
            "https://image.tmdb.org/t/p/w500/header1.jpg",
            "https://image.tmdb.org/t/p/w500/header2.jpg"
        )
    )

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk()
        getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(episodeRepository)
    }

    @Test
    fun `when getting episode details should return correct episode`() = runTest {
        // Given
        val tvId = 12345L
        val seasonNumber = 1
        val episodeNumber = 1

        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns sampleEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(sampleEpisode)
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `when getting episode details should call repository with correct parameters`() = runTest {
        // Given
        val tvId = 12345L
        val seasonNumber = 2
        val episodeNumber = 5

        coEvery {
            episodeRepository.getEpisodeDetails(any(), any(), any())
        } returns sampleEpisode

        // When
        getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        coVerify(exactly = 1) {
            episodeRepository.getEpisodeDetails(
                eq(tvId),
                eq(seasonNumber),
                eq(episodeNumber)
            )
        }
    }

    @Test
    fun `when repository throws exception should propagate it`() = runTest {
        // Given
        val tvId = 12345L
        val seasonNumber = 1
        val episodeNumber = 1
        val expectedException = RuntimeException("Network error")

        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } throws expectedException

        // When & Then
        assertThrows<RuntimeException> {
            getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)
        }.apply {
            assertThat(message).isEqualTo("Network error")
        }
    }

    @Test
    fun `when getting different episode should return correct data`() = runTest {
        // Given
        val tvId = 67890L
        val seasonNumber = 3
        val episodeNumber = 7
        val differentEpisode = sampleEpisode.copy(
            id = 456L,
            title = "The Long Night",
            episodeNumber = 3,
            rating = 9.8
        )

        coEvery {
            episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
        } returns differentEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(differentEpisode)
        assertThat(result.id).isEqualTo(456L)
        assertThat(result.title).isEqualTo("The Long Night")
        assertThat(result.rating).isEqualTo(9.8)
    }
}