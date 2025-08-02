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

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowSeasonEpisodesUseCase: GetTvShowSeasonEpisodesUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowSeasonEpisodesUseCase = GetTvShowSeasonEpisodesUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase() should return episodes when repository returns data for valid season`() = runTest {
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
        assertThat(result).containsExactlyElementsIn(expectedEpisodes)
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase() should return empty list when repository returns no episodes for season`() = runTest {
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
    fun `getTvShowSeasonEpisodesUseCase() should make exactly one repository call per invocation`() = runTest {
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
    fun `getTvShowSeasonEpisodesUseCase() should return different episodes for different seasons`() = runTest {
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
        assertThat(resultSeason1).isNotEqualTo(resultSeason2)
        assertThat(resultSeason1.first().id).isEqualTo(1L)
        assertThat(resultSeason2.first().id).isEqualTo(2L)
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase() should return complete episode details when available`() = runTest {
        // Given
        val tvId = 5L
        val seasonNumber = 1
        val detailedEpisode = sampleEpisode.copy(
            id = 101,
            title = "Detailed Episode",
            overview = "This is a detailed episode description",
            episodeNumber = 3,
            rating = 9.5,
            duration = "60",
            releasedDate = LocalDate(2023, 2, 15)
        )
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns listOf(detailedEpisode)

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        with(result.first()) {
            assertThat(title).isEqualTo("Detailed Episode")
            assertThat(overview).isEqualTo("This is a detailed episode description")
            assertThat(episodeNumber).isEqualTo(3)
            assertThat(rating).isEqualTo(9.5)
            assertThat(duration).isEqualTo("60")
            assertThat(releasedDate).isEqualTo(LocalDate(2023, 2, 15))
        }
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase() should handle large number of episodes in season`() = runTest {
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
        assertThat(result.first().title).isEqualTo("Episode 1")
        assertThat(result.last().title).isEqualTo("Episode 50")
        assertThat(result.map { it.episodeNumber }).containsExactlyElementsIn(1..50)
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase() should preserve episode genres when present`() = runTest {
        // Given
        val tvId = 7L
        val seasonNumber = 1
        val episodeWithGenres = sampleEpisode.copy(
            genres = listOf(
                Genre(id = 1, "Drama"),
                Genre(id = 2, "Action"),
                Genre(id = 3, "Thriller")
            )
        )
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns listOf(episodeWithGenres)

        // When
        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        // Then
        assertThat(result.first().genres).hasSize(3)
        assertThat(result.first().genres.map { it.name })
            .containsExactly("Drama", "Action", "Thriller")
    }

    private companion object {
        val sampleEpisode = Episode(
            id = 1L,
            title = "Sample Episode",
            overview = "Sample episode description",
            episodeNumber = 1,
            rating = 8.7,
            duration = "45",
            releasedDate = LocalDate(2023, 1, 1),
            trailerUrl = "https://example.com/trailer.mp4",
            currentSeason = 1,
            genres = listOf(Genre(id = 1, "Drama"), Genre(id = 2, "Action")),
            headerPictures = listOf(
                "https://example.com/episode.jpg",
                "https://example.com/episode2.jpg"
            )
        )
    }
}