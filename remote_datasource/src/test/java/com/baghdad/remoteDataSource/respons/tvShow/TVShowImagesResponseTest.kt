package com.baghdad.remoteDataSource.response.tvShow

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TVShowImagesResponseTest {

    @Test
    fun `should create TVShowImagesResponse with full data when all fields are provided`() {
        // Given
        val expectedBackdrops = listOf(
            ImageResponse(filePath = "/backdrop1.jpg"),
            ImageResponse(filePath = "/backdrop2.jpg")
        )
        val expectedLogos = listOf(
            ImageResponse(filePath = "/logo1.png")
        )
        val expectedPosters = listOf(
            ImageResponse(filePath = "/poster1.jpg"),
            ImageResponse(filePath = "/poster2.jpg")
        )

        // When
        val response = TVShowImagesResponse(
            id = 200,
            backdrops = expectedBackdrops,
            logos = expectedLogos,
            posters = expectedPosters
        )

        // Then
        assertThat(response.id).isEqualTo(200)
        assertThat(response.backdrops).isNotNull()
        assertThat(response.backdrops!!.size).isEqualTo(2)
        assertThat(response.backdrops!!.map { it.filePath }).containsExactly(
            "/backdrop1.jpg",
            "/backdrop2.jpg"
        )
        assertThat(response.logos).isNotNull()
        assertThat(response.logos!!.size).isEqualTo(1)
        assertThat(response.logos!!.first().filePath).isEqualTo("/logo1.png")
        assertThat(response.posters).isNotNull()
        assertThat(response.posters!!.map { it.filePath }).containsExactly(
            "/poster1.jpg",
            "/poster2.jpg"
        )
    }

    @Test
    fun `should create TVShowImagesResponse with default values when no fields are provided`() {
        // When
        val response = TVShowImagesResponse()

        // Then
        assertThat(response.id).isNull()
        assertThat(response.backdrops).isNull()
        assertThat(response.logos).isNull()
        assertThat(response.posters).isNull()
    }

    @Test
    fun `should create TVShowImagesResponse with empty lists when provided`() {
        // When
        val response = TVShowImagesResponse(
            id = 300,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        )

        // Then
        assertThat(response.id).isEqualTo(300)
        assertThat(response.backdrops).isEmpty()
        assertThat(response.logos).isEmpty()
        assertThat(response.posters).isEmpty()
    }

    @Test
    fun `should create TVShowImagesResponse with partial data when some fields are provided`() {
        // When
        val response = TVShowImagesResponse(
            id = 400,
            backdrops = listOf(ImageResponse(filePath = "/only_backdrop.jpg"))
        )

        // Then
        assertThat(response.id).isEqualTo(400)
        assertThat(response.backdrops).isNotNull()
        assertThat(response.backdrops!!.first().filePath).isEqualTo("/only_backdrop.jpg")
        assertThat(response.logos).isNull()
        assertThat(response.posters).isNull()
    }
}