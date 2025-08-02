package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.mapper.tvShow.toPagedTvShowDtos
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TopRatedTvShowSearchMapperTest {

    @Test
    fun `should map TopRatedTvShowSearchResponse to TvShowDtos when data is valid`() {
        // Given
        val response = TopRatedTvShowSearchResponse(
            page = 1,
            totalPages = 10,
            results = listOf(
                TopRatedTvShowSearchResponse.Result(
                    id = 100,
                    title = "Breaking Bad",
                    genreIds = listOf(18, 80),
                    voteAverage = 9.5,
                    releaseDate = "2008-01-20",
                    overview = "A chemistry teacher becomes a meth cook.",
                    posterPath = "/breakingbad.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).hasSize(1)
        val tvShow = result.data.first()
        assertThat(tvShow.id).isEqualTo(result.data.first().id)
        assertThat(tvShow.title).isEqualTo(result.data.first().title)
        assertThat(tvShow.genres).hasSize(2)
        assertThat(tvShow.imdbRating).isEqualTo(result.data.first().imdbRating)
        assertThat(tvShow.releaseDate).isEqualTo(result.data.first().releaseDate)
        assertThat(tvShow.posterPictureURL)
            .isEqualTo(result.data.first().posterPictureURL)
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = TopRatedTvShowSearchResponse(
            page = 1,
            totalPages = 5,
            results = null
        )

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip TvShow when id is null`() {
        // Given
        val response = TopRatedTvShowSearchResponse(
            results = listOf(
                TopRatedTvShowSearchResponse.Result(
                    id = null,
                    title = "Invalid Show",
                    genreIds = listOf(12),
                    voteAverage = 7.0,
                    releaseDate = "2020-01-01",
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = TopRatedTvShowSearchResponse(
            results = listOf(
                TopRatedTvShowSearchResponse.Result(
                    id = 0,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).hasSize(1)
        val tvShow = result.data.first()
        assertThat(tvShow.id).isEqualTo(0)
        assertThat(tvShow.title).isEmpty()
        assertThat(tvShow.genres).isEmpty()
        assertThat(tvShow.imdbRating).isEqualTo(0.0)
        assertThat(tvShow.releaseDate).isEmpty()
        assertThat(tvShow.overview).isEmpty()
        assertThat(tvShow.posterPictureURL).isEmpty()
    }
}
