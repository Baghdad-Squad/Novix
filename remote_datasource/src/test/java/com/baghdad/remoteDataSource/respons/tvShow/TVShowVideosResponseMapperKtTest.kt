package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.mapper.tvShow.mapToYoutubeURL
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TVShowVideosMapperTest {

    @Test
    fun `should return YouTube trailer URL when available`() {
        // Given
        val response = TVShowVideosResponse(
            results = listOf(
                TVShowVideosResponse.Result(key = "abc123", site = "YouTube", type = "Trailer")
            )
        )

        // When
        val result = response.mapToYoutubeURL()

        // Then
        assertThat(result).isEqualTo("https://www.youtube.com/watch?v=abc123")
    }

    @Test
    fun `should return empty string when YouTube video is not a Trailer`() {
        // Given
        val response = TVShowVideosResponse(
            results = listOf(
                TVShowVideosResponse.Result(key = "xyz789", site = "YouTube", type = "Teaser")
            )
        )

        // When
        val result = response.mapToYoutubeURL()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when video site is not YouTube`() {
        // Given
        val response = TVShowVideosResponse(
            results = listOf(
                TVShowVideosResponse.Result(key = "def456", site = "Vimeo", type = "Trailer")
            )
        )

        // When
        val result = response.mapToYoutubeURL()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when results is null`() {
        // Given
        val response = TVShowVideosResponse(results = null)

        // When
        val result = response.mapToYoutubeURL()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when YouTube trailer key is null`() {
        // Given
        val response = TVShowVideosResponse(
            results = listOf(
                TVShowVideosResponse.Result(key = null, site = "YouTube", type = "Trailer")
            )
        )

        // When
        val result = response.mapToYoutubeURL()

        // Then
        assertThat(result).isEmpty()
    }

}
