package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieImageResponseMapperTest {

    companion object {
        private const val FILE_PATH_1 = "/image1.jpg"
        private const val FILE_PATH_2 = "/image2.jpg"
    }

    @Test
    fun `toImageUrl should return list of full URLs when backdrops are present`() {
        val response = MovieImageResponse(
            id = 1L,
            backdrops = listOf(
                MovieImageResponse.ImageResponse(FILE_PATH_1),
                MovieImageResponse.ImageResponse(FILE_PATH_2)
            )
        )

        val result = response.toImageUrl()

        assertThat(result).containsExactly(
            getImageUrlFromPath(FILE_PATH_1),
            getImageUrlFromPath(FILE_PATH_2)
        ).inOrder()
    }

    @Test
    fun `toImageUrl should return empty list when backdrops is null`() {
        val response = MovieImageResponse(
            id = 1L,
            backdrops = null
        )

        val result = response.toImageUrl()

        assertThat(result).isEmpty()
    }

    @Test
    fun `toImageUrl should handle null file paths`() {
        val response = MovieImageResponse(
            id = 1L,
            backdrops = listOf(
                MovieImageResponse.ImageResponse(null),
                MovieImageResponse.ImageResponse(FILE_PATH_1)
            )
        )

        val result = response.toImageUrl()

        assertThat(result).containsExactly(
            getImageUrlFromPath(null),
            getImageUrlFromPath(FILE_PATH_1)
        )
    }
}
