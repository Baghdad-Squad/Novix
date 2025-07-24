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

    private lateinit var  episodeRepository : EpisodeRepository
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase
    private val expectedEpisode = Episode(
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
            Genre(
                id = 1L,
                name = "Drama",
            ),
            Genre(
                id = 2L,
                name = "Action",
            ),
            Genre(
                id = 3L,
                name = "Comedy",
            )
        ),
        headerPictures = listOf(
            "https://example.com/header1.jpg",
            "https://example.com/header2.jpg"
        ),
    )

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(episodeRepository)
    }

    @Test
    fun `getEpisodeDetailsUseCase should return episode details from repository`() = runTest {
        // Given
        val tvId = 123L
        val seasonNumber = 1
        val episodeNumber = 1
        coEvery { episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber) } returns expectedEpisode

        // When
        val result = getEpisodeDetailsUseCase(tvId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(expectedEpisode)
        coVerify { episodeRepository.getEpisodeDetails(tvId, seasonNumber, episodeNumber) }
    }



}