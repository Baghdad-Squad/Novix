package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.mapper.tvShow.toPagedTvShowDtos
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse.TrendingTvShow
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingTvShowsMapperTest {

    @Test
    fun `should map valid TrendingTvShow to TvShowDto`() {
        // Given
        val response = TrendingTvShowsResponse(
            page = 1,
            totalPages = 5,
            results = listOf(
                TrendingTvShow(
                    id = 200,
                    name = "Stranger Things",
                    genreIds = listOf(18, 9648),
                    voteAverage = 8.7,
                    firstAirDate = "2016-07-15",
                    overview = "Mystery sci-fi show",
                    posterPath = "/stranger.jpg"
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
        assertThat(tvShow.releaseDate).isEqualTo(result.data.first().releaseDate)
        assertThat(tvShow.posterPictureURL)
            .isEqualTo(result.data.first().posterPictureURL)
        assertThat(tvShow.imdbRating).isEqualTo(8.7)
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = TrendingTvShowsResponse(
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
        val response = TrendingTvShowsResponse(
            results = listOf(
                TrendingTvShow(
                    id = null,
                    name = "Invalid Show",
                    genreIds = listOf(12),
                    voteAverage = 6.0,
                    firstAirDate = "2020-01-01",
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
        val response = TrendingTvShowsResponse(
            results = listOf(
                TrendingTvShow(
                    id = 0,
                    name = null,
                    genreIds = null,
                    voteAverage = null,
                    firstAirDate = null,
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
        assertThat(tvShow.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500")
    }
}
