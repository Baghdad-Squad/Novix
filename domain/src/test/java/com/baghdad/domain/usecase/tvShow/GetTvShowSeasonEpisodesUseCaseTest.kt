package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
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

class GetTvShowSeasonEpisodesUseCaseTest {

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowSeasonEpisodesUseCase = GetTvShowSeasonEpisodesUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowSeasonEpisodes returns list of episodes for valid season`() = runTest {
        // Given
        val tvId = 1L
        val seasonNumber = 1
        val expectedEpisodes = listOf(
            sampleEpisode.copy(id = 1, title = "Pilot"),
            sampleEpisode.copy(id = 2, title = "Second Episode")
        )
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns expectedEpisodes

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.title }).containsExactly("Pilot", "Second Episode")
    }

    @Test
    fun `getTvShowSeasonEpisodes returns empty list for season with no episodes`() = runTest {
        // Given
        val tvId = 2L
        val seasonNumber = 5
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns emptyList()

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowSeasonEpisodes makes exactly one repository call`() = runTest {
        // Given
        val tvId = 3L
        val seasonNumber = 2
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns emptyList()

        // When
        getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) }
    }

    @Test
    fun `getTvShowSeasonEpisodes returns different results for different seasons`() = runTest {
        // Given
        val tvId = 4L
        val season1Episodes = listOf(sampleEpisode.copy(id = 1, currentSeason = 1))
        val season2Episodes = listOf(sampleEpisode.copy(id = 2, currentSeason = 2))
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, 1) } returns season1Episodes
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, 2) } returns season2Episodes

        // When
        val resultSeason1 = getTvShowSeasonEpisodesUseCase(tvId, 1)
        val resultSeason2 = getTvShowSeasonEpisodesUseCase(tvId, 2)

        // Then
        assertThat(resultSeason1[0].id).isEqualTo(1L)
        assertThat(resultSeason2[0].id).isEqualTo(2L)
    }

    @Test
    fun `getTvShowSeasonEpisodes returns correct episode details`() = runTest {
        // Given
        val tvId = 5L
        val seasonNumber = 1
        val detailedEpisode = sampleEpisode.copy(
            id = 101,
            title = "Detailed Episode",
            overview = "This is a detailed episode description",
            episodeNumber = 3,
        )
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns listOf(detailedEpisode)

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        assertThat(result[0].title).isEqualTo("Detailed Episode")
        assertThat(result[0].overview).isEqualTo("This is a detailed episode description")
        assertThat(result[0].episodeNumber).isEqualTo(3)
    }

    @Test
    fun `getTvShowSeasonEpisodes handles large season with many episodes`() = runTest {
        // Given
        val tvId = 6L
        val seasonNumber = 1
        val largeEpisodeList = List(50) { index ->
            sampleEpisode.copy(
                id = index.toLong(),
                title = "Episode ${index + 1}",
                episodeNumber = index + 1
            )
        }
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns largeEpisodeList

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        assertThat(result).hasSize(50)
        assertThat(result.last().title).isEqualTo("Episode 50")
    }

    companion object {
        private lateinit var tvShowRepository: TvShowRepository
        private lateinit var getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase

        private val sampleEpisode = Episode(
            id = 1L,
            title = "Sample Episode",
            overview = "Sample episode description",
            episodeNumber = 1,
            rating = 8.7,
            duration = "45",
            releasedDate = LocalDate(2023, 1, 1),
            trailerUrl = "https://example.com/trailer.mp4",
            currentSeason = 1,
            genres = listOf(Genre(id = 1, "Drama"),Genre( id = 2, name = "Action")),
            headerPictures = listOf(
            "https://example.com/episode.jpg",
            "https://example.com/episode2.jpg"
            )
        )
    }
}