package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse.Result
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingMovieResponseMapperTest {

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = TrendingMovieResponse(page = 1, totalPages = 5, results = null)

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNotNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should skip movies when id is null`() {
        // Given
        val response = TrendingMovieResponse(
            page = 1,
            totalPages = 3,
            results = listOf(Result(id = null))
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should map movies correctly when results contain valid data`() {
        // Given
        val response = TrendingMovieResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                Result(
                    id = 101L,
                    title = "Dune",
                    posterPath = "/poster.jpg",
                    backdropPath = "/backdrop.jpg",
                    overview = "Epic sci-fi movie",
                    releaseDate = "2021-10-22",
                    voteAverage = 8.2,
                    popularity = 100.5,
                    genreIds = listOf(12, 18)
                )
            )
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result.data).hasSize(1)
        val movie = result.data.first()
        assertThat(movie.id).isEqualTo(response.results?.first()?.id)
        assertThat(movie.title).isEqualTo(response.results?.first()?.title)
        assertThat(movie.posterPictureURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500${response.results?.first()?.posterPath}")
        assertThat(movie.trailerURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500${response.results?.first()?.backdropPath}")
        assertThat(movie.overview).isEqualTo(response.results?.first()?.overview)
        assertThat(movie.releaseDate).isEqualTo(response.results?.first()?.releaseDate)
        assertThat(movie.imdbRating).isEqualTo(response.results?.first()?.voteAverage)
        assertThat(movie.userRating).isEqualTo(response.results?.first()?.popularity)
        assertThat(movie.genres).hasSize(2)
        assertThat(movie.genres.first().id)
            .isEqualTo(response.results?.first()?.genreIds?.first()?.toLong())
    }

    @Test
    fun `should set nextKey to null when on last page`() {
        // Given
        val response = TrendingMovieResponse(
            page = 3,
            totalPages = 3,
            results = listOf(Result(id = 1, title = "Last Page Movie"))
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNotNull()
    }

    @Test
    fun `should use defaults when optional fields are null`() {
        // Given
        val result = Result(
            id = 200L,
            title = null,
            posterPath = null,
            backdropPath = null,
            overview = null,
            releaseDate = null,
            voteAverage = null,
            popularity = null,
            genreIds = null
        )

        // When
        val movie = result.toDto()

        // Then
        assertThat(movie.title).isEmpty()
        assertThat(movie.posterPictureURL).isEmpty()
        assertThat(movie.trailerURL).isEmpty()
        assertThat(movie.overview).isEmpty()
        assertThat(movie.releaseDate).isEmpty()
        assertThat(movie.imdbRating).isEqualTo(0.0)
        assertThat(movie.userRating).isEqualTo(0.0)
        assertThat(movie.genres).isEmpty()
    }
}