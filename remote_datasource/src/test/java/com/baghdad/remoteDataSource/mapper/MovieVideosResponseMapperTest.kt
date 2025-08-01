package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.mapper.movie.mapToYoutubeURL
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieVideosResponseMapperTest {
    @Test
    fun `should return YouTube trailer url when available`() {
        // Given
        val response = MovieVideosResponse(
            results = listOf(
                MovieVideosResponse.Result(site = "YouTube", type = "Trailer", key = "abc123")
            )
        )

        // When
        val url = response.mapToYoutubeURL()

        // Then
        assertThat(url).isEqualTo("https://www.youtube.com/watch?v=abc123")
    }

    @Test
    fun `should return empty string when no YouTube trailer found`() {
        // Given
        val response = MovieVideosResponse(
            results = listOf(
                MovieVideosResponse.Result(site = "Vimeo", type = "Clip", key = "xyz789")
            )
        )

        // When
        val url = response.mapToYoutubeURL()

        // Then
        assertThat(url).isEmpty()
    }
}