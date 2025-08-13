package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeVideosResponseMapperTest {


    companion object {
        private const val YOUTUBE_KEY = "abcd1234"
        private const val EXPECTED_URL = "https://www.youtube.com/watch?v=$YOUTUBE_KEY"

        private val YOUTUBE_TRAILER = EpisodeVideosResponse.Result(
            id = "1",
            key = YOUTUBE_KEY,
            name = "Official Trailer",
            official = true,
            publishedAt = "2023-09-15",
            site = "YouTube",
            size = 1080,
            type = "Trailer"
        )

        private val YOUTUBE_NON_TRAILER = YOUTUBE_TRAILER.copy(type = "Teaser")
        private val NON_YOUTUBE_TRAILER = YOUTUBE_TRAILER.copy(site = "Vimeo")
        private val NULL_KEY_TRAILER = YOUTUBE_TRAILER.copy(key = null)
    }

    @Test
    fun `should return youtube trailer url when valid trailer exists`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = listOf(YOUTUBE_TRAILER)
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEqualTo(EXPECTED_URL)
    }

    @Test
    fun `should return empty string when no results`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = emptyList()
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when results is null`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = null
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when youtube video is not a trailer`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = listOf(YOUTUBE_NON_TRAILER)
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when trailer is not from youtube`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = listOf(NON_YOUTUBE_TRAILER)
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty string when youtube trailer has null key`() {
        val response = EpisodeVideosResponse(
            id = 10L,
            results = listOf(NULL_KEY_TRAILER)
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return first matching youtube trailer when multiple are present`() {
        val extraTrailer = YOUTUBE_TRAILER.copy(key = "xyz987")
        val response = EpisodeVideosResponse(
            id = 10L,
            results = listOf(YOUTUBE_NON_TRAILER, extraTrailer, YOUTUBE_TRAILER)
        )

        val result = response.mapToYoutubeTrailerUrl()

        assertThat(result).isEqualTo("https://www.youtube.com/watch?v=xyz987")
    }
}
