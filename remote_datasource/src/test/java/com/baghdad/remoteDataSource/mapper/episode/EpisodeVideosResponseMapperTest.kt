package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse.Result
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeVideosResponseMapperTest {

    @Test
    fun `should return trailer url when YouTube trailer exists`() {
        // Given
        val response = EpisodeVideosResponse(
            results = listOf(
                Result(site = "YouTube", type = "Trailer", key = "TRAILER_KEY")
            )
        )

        // When
        val url = response.mapToYoutubeTrailerUrl()

        // Then
        assertThat(url).isEqualTo("TRAILER_KEY")
    }

    @Test
    fun `should return youtube url when no trailer but other YouTube video exists`() {
        // Given
        val response = EpisodeVideosResponse(
            results = listOf(
                Result(site = "YouTube", type = "Clip", key = "CLIP_KEY")
            )
        )

        // When
        val url = response.mapToYoutubeTrailerUrl()

        // Then
        assertThat(url).isEqualTo("https://www.youtube.com/watch?v=CLIP_KEY")
    }

    @Test
    fun `should return empty string when no YouTube videos exist`() {
        // Given
        val response = EpisodeVideosResponse(
            results = listOf(
                Result(site = "Vimeo", type = "Trailer", key = "VIMEO_KEY")
            )
        )

        // When
        val url = response.mapToYoutubeTrailerUrl()

        // Then
        assertThat(url).isEmpty()
    }

    @Test
    fun `should return empty string when results is null`() {
        // Given
        val response = EpisodeVideosResponse(results = null)

        // When
        val url = response.mapToYoutubeTrailerUrl()

        // Then
        assertThat(url).isEmpty()
    }

    @Test
    fun `should return empty string when results is empty`() {
        // Given
        val response = EpisodeVideosResponse(results = emptyList())

        // When
        val url = response.mapToYoutubeTrailerUrl()

        // Then
        assertThat(url).isEmpty()
    }
}
