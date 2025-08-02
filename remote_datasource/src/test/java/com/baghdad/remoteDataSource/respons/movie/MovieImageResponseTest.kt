package com.baghdad.remoteDataSource.respons.movie

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieImageResponseTest {

    @Test
    fun `should create MovieImageResponse with full data when all fields are provided`() {
        // Given
        val expectedBackdrops = listOf(
            ImageResponse(filePath = "/poster1.jpg"),
            ImageResponse(filePath = "/poster2.jpg")
        )

        // When
        val response = MovieImageResponse(
            id = 101,
            backdrops = expectedBackdrops
        )

        // Then
        assertThat(response.id).isEqualTo(101)
        assertThat(response.backdrops).isNotNull()
        assertThat(response.backdrops!!.size).isEqualTo(2)
        assertThat(response.backdrops[0].filePath).isEqualTo("/poster1.jpg")
        assertThat(response.backdrops[1].filePath).isEqualTo("/poster2.jpg")
    }

    @Test
    fun `should create MovieImageResponse with default values when no fields are provided`() {
        // Given & When
        val response = MovieImageResponse()

        // Then
        assertThat(response.id).isEqualTo(0)
        assertThat(response.backdrops).isNull()
    }

    @Test
    fun `should create MovieImageResponse with empty backdrops list when backdrops provided as empty`() {
        // Given
        val response = MovieImageResponse(
            id = 200,
            backdrops = emptyList()
        )

        // Then
        assertThat(response.id).isEqualTo(200)
        assertThat(response.backdrops).isEmpty()
    }

    @Test
    fun `should create ImageResponse with default values when no fields are provided`() {
        // Given & When
        val image = ImageResponse()

        // Then
        assertThat(image.filePath).isNull()
    }

    @Test
    fun `should handle multiple ImageResponse objects inside backdrops`() {
        // Given
        val backdrops = listOf(
            ImageResponse(filePath = "/image1.jpg"),
            ImageResponse(filePath = "/image2.jpg"),
            ImageResponse(filePath = "/image3.jpg")
        )

        // When
        val response = MovieImageResponse(
            id = 300,
            backdrops = backdrops
        )

        // Then
        assertThat(response.id).isEqualTo(300)
        assertThat(response.backdrops!!.size).isEqualTo(3)
        assertThat(response.backdrops[2].filePath).isEqualTo("/image3.jpg")
    }
}
