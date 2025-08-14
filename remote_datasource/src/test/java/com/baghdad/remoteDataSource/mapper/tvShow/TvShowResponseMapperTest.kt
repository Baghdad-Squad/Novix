package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowResponseMapperTest {

    @Test
    fun `should map TvShowResponse to TvShowDtos when results contain valid data`() {
        // Given
        val response = TvShowResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                TVShowDetailsResponse(
                    id = 101,
                    name = "Breaking Bad",
                    overview = "A chemistry teacher turns to crime.",
                    firstAirDate = "2008-01-20",
                    voteAverage = 9.5,
                    posterPath = "/poster.jpg"
                )
            )
        )

        // When
        val result: PagedResultDto<TvShowDto> = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).hasSize(1)
        val tvShow = result.data.first()
        assertThat(tvShow.id).isEqualTo(101)
        assertThat(tvShow.title).isEqualTo("Breaking Bad")
        assertThat(tvShow.overview).isEqualTo("A chemistry teacher turns to crime.")
        assertThat(tvShow.releaseDate).isEqualTo("2008-01-20")
        assertThat(tvShow.imdbRating).isEqualTo(9.5)
        assertThat(tvShow.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = TvShowResponse(page = 1, totalPages = 1, results = null)

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip TvShow when id is null`() {
        // Given
        val response = TvShowResponse(
            page = 2,
            totalPages = 4,
            results = listOf(
                TVShowDetailsResponse(
                    id = null,
                    name = "Invalid Show",
                    overview = "Should be skipped",
                    firstAirDate = "2020-01-01",
                    voteAverage = 6.0,
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos()

        // Then
        assertThat(result.data).isEmpty()
    }
}
