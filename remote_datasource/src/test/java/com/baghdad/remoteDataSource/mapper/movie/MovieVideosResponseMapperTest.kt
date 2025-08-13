package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieVideosResponseMapperTest {
    @Test
    fun `should return YouTube trailer url when available`() {
        val response = MovieVideosResponse(
            results = listOf(
                MovieVideosResponse.Result(site = "YouTube", type = "Trailer", key = "abc123")
            )
        )

        val url = response.mapToYoutubeURL()

        // Then
        assertThat(url).isEqualTo("https://www.youtube.com/watch?v=abc123")
    }

    @Test
    fun `should return empty string when no YouTube trailer found`() {
        val response = MovieVideosResponse(
            results = listOf(
                MovieVideosResponse.Result(site = "Vimeo", type = "Clip", key = "xyz789")
            )
        )

        val url = response.mapToYoutubeURL()

        assertThat(url).isEmpty()
    }
}