package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.mapper.movie.toPagedMovieDtos
import com.baghdad.remoteDataSource.response.MovieResult
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SimilarMovieMapperKtTest {

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = SimilarMovieResponse(page = 1, totalPages = 3, results = null)

        // When
        val result = response.toPagedMovieDtos()

        // When
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip movies when id is null`() {
        // Given
        val response = SimilarMovieResponse(
            page = 1,
            totalPages = 3,
            results = listOf(MovieResult(id = null))
        )
        // When
        val result = response.toPagedMovieDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should map movies correctly when id is valid`() {
        // Given
        val response = SimilarMovieResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                MovieResult(id = 42, title = "Interstellar", genreIds = listOf(12, 18))
            )
        )

        // When
        val result = response.toPagedMovieDtos()

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data.first().id).isEqualTo(42)
        assertThat(result.nextKey).isNotNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should set nextKey to null when on last page`() {
        // Given
        val response = SimilarMovieResponse(
            page = 3,
            totalPages = 3,
            results = listOf(MovieResult(id = 7, title = "Tenet"))
        )
        // When
        val result = response.toPagedMovieDtos()

        // Then
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNotNull()
    }

    @Test
    fun `should handle null page and totalPages safely`() {
        // Given
        val response = SimilarMovieResponse(page = null, totalPages = null, results = listOf())

        // When
        val result = response.toPagedMovieDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }
}
