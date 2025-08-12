package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.repository.model.EpisodeDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeResponseMapperTest {

    companion object {
        private val COMPLETE_EPISODE = SeasonDetailResponse.EpisodeResponse(
            id = 1L,
            airDate = "2023-10-15",
            episodeNumber = 5,
            name = "The Breakthrough",
            overview = "Key events unfold in this episode.",
            runtime = 42,
            seasonNumber = 3,
            voteAverage = 9.1
        )

        private val NULL_VALUES_EPISODE = SeasonDetailResponse.EpisodeResponse(
            id = 2L,
            airDate = null,
            episodeNumber = null,
            name = null,
            overview = null,
            runtime = null,
            seasonNumber = null,
            voteAverage = null
        )

        private val NULL_ID_EPISODE = SeasonDetailResponse.EpisodeResponse(
            id = null,
            airDate = "2024-01-01",
            episodeNumber = 1,
            name = "Pilot",
            overview = "The first episode.",
            runtime = 50,
            seasonNumber = 1,
            voteAverage = 7.5
        )

        private val EXPECTED_COMPLETE_DTO = EpisodeDto(
            id = 1L,
            title = "The Breakthrough",
            episodeNumber = 5,
            rating = 9.1,
            duration = "42",
            releasedDate = "2023-10-15",
            currentSeason = 3,
            overview = "Key events unfold in this episode.",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        private val EXPECTED_NULL_VALUES_DTO = EpisodeDto(
            id = 2L,
            title = "",
            episodeNumber = 0,
            rating = 0.0,
            duration = "",
            releasedDate = null,
            currentSeason = 0,
            overview = "",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        private val EXPECTED_NULL_ID_DTO = EpisodeDto(
            id = -1L,
            title = "Pilot",
            episodeNumber = 1,
            rating = 7.5,
            duration = "50",
            releasedDate = "2024-01-01",
            currentSeason = 1,
            overview = "The first episode.",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )
    }

    @Test
    fun `should map complete episode list to EpisodeDto list`() {
        val response = SeasonDetailResponse(episodes = listOf(COMPLETE_EPISODE))

        val result = response.toEpisodeDto()

        assertThat(result).containsExactly(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should map null values in episode fields to default values`() {
        val response = SeasonDetailResponse(episodes = listOf(NULL_VALUES_EPISODE))

        val result = response.toEpisodeDto()

        assertThat(result).containsExactly(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should return empty list when episodes is null`() {
        val response = SeasonDetailResponse(episodes = null)

        val result = response.toEpisodeDto()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should ignore episodes with null id`() {
        val episodeWithNullId = NULL_ID_EPISODE.copy(id = null)
        val response = SeasonDetailResponse(episodes = listOf(episodeWithNullId))

        val result = response.toEpisodeDto()

        assertThat(result).isEmpty()
    }
}
