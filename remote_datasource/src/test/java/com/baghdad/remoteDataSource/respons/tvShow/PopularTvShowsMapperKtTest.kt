package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.mapper.tvShow.toTvShowDtos
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PopularTvShowsMapperTest {

    @Test
    fun `should map PopularTvShowsResponse to TvShowDtos when data is valid`() {
        // Given
        val response = PopularTvShowsResponse(
            results = listOf(
                PopularTvShowsResponse.Result(
                    id = 101,
                    name = "Stranger Things",
                    genreIds = listOf(18, 9648),
                    voteAverage = 8.7,
                    firstAirDate = "2016-07-15",
                    posterPath = "/poster.jpg"
                )
            )
        )

        // When
        val result = response.toTvShowDtos()

        // Then
        assertThat(result).hasSize(1)
        val tvShow = result.first()
        assertThat(tvShow.id).isEqualTo(result.first().id)
        assertThat(tvShow.title).isEqualTo(result.first().title)
        assertThat(tvShow.genres).hasSize(2)
        assertThat(tvShow.imdbRating).isEqualTo(result.first().imdbRating)
        assertThat(tvShow.releaseDate).isEqualTo(result.first().releaseDate)
        assertThat(tvShow.posterPictureURL)
            .isEqualTo(result.first().posterPictureURL)
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = PopularTvShowsResponse(results = null)

        // When
        val result = response.toTvShowDtos()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should skip TvShow when id is null`() {
        // Given
        val response = PopularTvShowsResponse(
            results = listOf(
                PopularTvShowsResponse.Result(
                    id = null,
                    name = "Invalid Show",
                    genreIds = listOf(35),
                    voteAverage = 6.0,
                    firstAirDate = "2020-01-01",
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toTvShowDtos()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = PopularTvShowsResponse(
            results = listOf(
                PopularTvShowsResponse.Result(
                    id = 0,
                    name = null,
                    genreIds = null,
                    voteAverage = null,
                    firstAirDate = null,
                    posterPath = null
                )
            )
        )

        // When
        val result = response.toTvShowDtos()

        // Then
        assertThat(result).hasSize(1)
        val tvShow = result.first()
        assertThat(tvShow.id).isEqualTo(0)
        assertThat(tvShow.title).isEmpty()
        assertThat(tvShow.genres).isEmpty()
        assertThat(tvShow.imdbRating).isEqualTo(0.0)
        assertThat(tvShow.releaseDate).isEqualTo("0001-01-01")
        assertThat(tvShow.posterPictureURL).isEmpty()
    }
}
