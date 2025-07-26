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