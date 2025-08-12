package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.SimilarMovieResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SimilarMoviesMapperTest {
    companion object {
        private val COMPLETE_MOVIE_RESPONSE = SimilarMovieResponse.MovieResult(
            id = 123L,
            title = "The Dark Knight",
            genreIds = listOf(28L, 80L, 18L),
            voteAverage = 9.0,
            releaseDate = "2008-07-18",
            overview = "When the menace known as the Joker wreaks havoc...",
            posterPath = "/dark_knight.jpg"
        )

        private val EXPECTED_COMPLETE_DTO = MovieDto(
            id = 123L,
            title = "The Dark Knight",
            genres = listOf(
                GenreDto(28L, "", GenreDto.GenreType.MOVIE),
                GenreDto(80L, "", GenreDto.GenreType.MOVIE),
                GenreDto(18L, "", GenreDto.GenreType.MOVIE)
            ),
            imdbRating = 9.0,
            userRating = 0.0,
            releaseDate = "2008-07-18",
            overview = "When the menace known as the Joker wreaks havoc...",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/dark_knight.jpg",
            runtimeMinutes = 0,
            trailerURL = ""
        )

        private val MIXED_NULL_MOVIE_RESPONSE = SimilarMovieResponse.MovieResult(
            id = 456L,
            title = "Inception",
            genreIds = listOf(28L, 878L),
            voteAverage = 8.8,
            releaseDate = null,
            overview = "A thief who steals corporate secrets...",
            posterPath = "/inception.jpg"
        )

        private val EXPECTED_MIXED_NULL_DTO = MovieDto(
            id = 456L,
            title = "Inception",
            genres = listOf(
                GenreDto(28L, "", GenreDto.GenreType.MOVIE),
                GenreDto(878L, "", GenreDto.GenreType.MOVIE)
            ),
            imdbRating = 8.8,
            userRating = 0.0,
            releaseDate = "0001-01-01",
            overview = "A thief who steals corporate secrets...",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/inception.jpg",
            runtimeMinutes = 0,
            trailerURL = ""
        )

        private val COMPLETE_SIMILAR_MOVIES_RESPONSE = SimilarMovieResponse(
            results = listOf(COMPLETE_MOVIE_RESPONSE, MIXED_NULL_MOVIE_RESPONSE)
        )

        private val NULL_RESULTS_SIMILAR_MOVIES_RESPONSE = SimilarMovieResponse(
            results = null
        )

        private val EMPTY_RESULTS_SIMILAR_MOVIES_RESPONSE = SimilarMovieResponse(
            results = emptyList()
        )
    }

    @Test
    fun `should convert SimilarMovieResponse with valid results to list of MovieDtos`() {
        val result = COMPLETE_SIMILAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_COMPLETE_DTO, EXPECTED_MIXED_NULL_DTO)
    }

    @Test
    fun `should handle null results in SimilarMovieResponse by returning empty list`() {
        val result = NULL_RESULTS_SIMILAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle empty results in SimilarMovieResponse by returning empty list`() {
        val result = EMPTY_RESULTS_SIMILAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out items with null id in results list`() {
        val response = SimilarMovieResponse(
            results = listOf(
                COMPLETE_MOVIE_RESPONSE.copy(id = null),
                MIXED_NULL_MOVIE_RESPONSE
            )
        )
        val result = response.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_MIXED_NULL_DTO)
    }
}