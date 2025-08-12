package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.repository.model.EpisodeDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeDetailsMapperTest {

    companion object {
        private val COMPLETE_EPISODE_RESPONSE = EpisodeDetailsResponse(
            id = 101L,
            airDate = "2023-08-15",
            episodeNumber = 5,
            name = "The Heist",
            overview = "The crew attempts a risky robbery.",
            runtime = 45,
            seasonNumber = 2,
            voteAverage = 8.5
        )

        private val NULL_VALUES_EPISODE_RESPONSE = EpisodeDetailsResponse(
            id = 202L,
            airDate = null,
            episodeNumber = 0,
            name = null,
            overview = null,
            runtime = 0,
            seasonNumber = 0,
            voteAverage = 0.0
        )

        private val NULL_ID_EPISODE_RESPONSE = EpisodeDetailsResponse(
            id = null,
            airDate = "2024-01-01",
            episodeNumber = 1,
            name = "Pilot",
            overview = "The first episode.",
            runtime = 50,
            seasonNumber = 1,
            voteAverage = 7.5
        )

        private val EXPECTED_COMPLETE_EPISODE = EpisodeDto(
            id = 101,
            title = "The Heist",
            episodeNumber = 5,
            rating = 8.5,
            duration = "45",
            releasedDate = "2023-08-15",
            currentSeason = 2,
            overview = "The crew attempts a risky robbery.",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        private val EXPECTED_NULL_VALUES_EPISODE = EpisodeDto(
            id = 202,
            title = "",
            episodeNumber = 0,
            rating = 0.0,
            duration = "0",
            releasedDate = null,
            currentSeason = 0,
            overview = "",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        private val EXPECTED_NULL_ID_EPISODE = EpisodeDto(
            id = -1,
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
    fun `should convert complete episode response to EpisodeDto`() {
        val episodeResponse = COMPLETE_EPISODE_RESPONSE

        val result = episodeResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_EPISODE)
    }

    @Test
    fun `should handle null values by using default values`() {
        val episodeResponse = NULL_VALUES_EPISODE_RESPONSE

        val result = episodeResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_EPISODE)
    }

    @Test
    fun `should set id to -1 when id is null`() {
        val episodeResponse = NULL_ID_EPISODE_RESPONSE

        val result = episodeResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_ID_EPISODE)
    }
}
