package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class DiscoverMovieMapperTest {

    @Test
    fun `should map movies correctly when results contain valid data`() {
        // Given
        val response = DISCOVER_MOVIE_RESPONSE_VALID

        // When
        val result = response.toMovieDtos()

        // Then
        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result.first().id).isEqualTo(DISCOVER_MOVIE_RESULT_VALID.id)
        Truth.assertThat(result.first().title).isEqualTo(DISCOVER_MOVIE_RESULT_VALID.title)
        Truth.assertThat(result.first().genres.size)
            .isEqualTo(DISCOVER_MOVIE_RESULT_VALID.genreIds?.size)
        Truth.assertThat(result.first().posterPictureURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500${DISCOVER_MOVIE_RESULT_VALID.posterPath}")
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = DiscoverMovieResponse(results = null)

        // When
        val result = response.toMovieDtos()

        // Then
        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = DISCOVER_MOVIE_RESPONSE_NULL_FIELDS

        // When
        val result = response.toMovieDtos()

        // Then
        Truth.assertThat(result).hasSize(1)
        val movie = result.first()
        Truth.assertThat(movie.id).isEqualTo(0L)
        Truth.assertThat(movie.title).isEqualTo("Untitled")
        Truth.assertThat(movie.releaseDate).isEqualTo("0001-01-01")
        Truth.assertThat(movie.posterPictureURL).isEmpty()
        Truth.assertThat(movie.genres).isEmpty()
    }

    @Test
    fun `should skip movies when id is null`() {
        // Given
        val response = DiscoverMovieResponse(
            results = listOf(
                DiscoverMovieResponse.Result(
                    id = null,
                    title = "Invalid Movie",
                    genreIds = listOf(28L),
                    voteAverage = 7.5,
                    releaseDate = "2020-01-01",
                    overview = "Should be skipped",
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toMovieDtos()

        // Then
        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun `should use empty list when genreIds parameter is not provided`() {
        // Given
        val result = DiscoverMovieResponse.Result(
            id = 10L,
            title = "No Genres Param",
            genreIds = null,
            voteAverage = 6.5,
            releaseDate = "2021-12-12",
            overview = "Testing default param",
            posterPath = "/poster.jpg"
        )

        // When
        val movieDto = result.toDto(emptyList())

        // Then
        Truth.assertThat(movieDto.genres).isEmpty()
    }

    @Test
    fun `should map genreIds when elements are null safely`() {
        // Given
        val result = DiscoverMovieResponse.Result(
            id = 20L,
            title = "Null Genre Ids",
            genreIds = listOf(null, 15L),
            voteAverage = 7.0,
            releaseDate = "2022-05-05",
            overview = "Some overview",
            posterPath = "/poster2.jpg"
        )

        // When
        val movieDto = result.toDto(genreIds = result.genreIds)

        // Then
        Truth.assertThat(movieDto.genres.size).isEqualTo(2)
        Truth.assertThat(movieDto.genres.first().id).isEqualTo(0L)
        Truth.assertThat(movieDto.genres.last().id).isEqualTo(15L)
    }

    @Test
    fun `should use empty genres when genreIds is not passed`() {
        // Given
        val result = DiscoverMovieResponse.Result(
            id = 30L,
            title = "Default Param Test",
            genreIds = listOf(99L, 100L),
            voteAverage = 5.5,
            releaseDate = "2023-03-03",
            overview = "Testing default param handling",
            posterPath = "/poster3.jpg"
        )

        // When
        val movieDto = result.toDto()

        // Then
        Truth.assertThat(movieDto.genres).isEmpty()
    }

    companion object {
        private val DISCOVER_MOVIE_RESULT_VALID = DiscoverMovieResponse.Result(
            id = 1L,
            title = "Inception",
            genreIds = listOf(12L, 18L),
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
            overview = "A mind-bending thriller",
            posterPath = "/inception.jpg"
        )

        private val DISCOVER_MOVIE_RESULT_NULL_FIELDS = DiscoverMovieResponse.Result(
            id = 0L,
            title = null,
            genreIds = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        val DISCOVER_MOVIE_RESPONSE_VALID = DiscoverMovieResponse(
            results = listOf(DISCOVER_MOVIE_RESULT_VALID)
        )

        val DISCOVER_MOVIE_RESPONSE_NULL_FIELDS = DiscoverMovieResponse(
            results = listOf(DISCOVER_MOVIE_RESULT_NULL_FIELDS)
        )
    }
}