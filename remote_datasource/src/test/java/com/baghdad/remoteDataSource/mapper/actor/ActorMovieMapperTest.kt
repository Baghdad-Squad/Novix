package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.repository.model.MovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorMovieMapperTest {

    companion object {
        private val COMPLETE_MOVIES_RESPONSE = ActorMoviesResponse(
            cast = listOf(
                ActorMoviesResponse.ActorMovieDto(
                    id = 123L,
                    title = "The Great Movie",
                    genreIds = listOf(28L, 12L),
                    voteAverage = 8.5,
                    releaseDate = "2023-12-15",
                    overview = "An amazing movie about adventure.",
                    posterPath = "/great_movie_poster.jpg"
                ),
                ActorMoviesResponse.ActorMovieDto(
                    id = 456L,
                    title = "Another Film",
                    genreIds = listOf(18L),
                    voteAverage = 7.2,
                    releaseDate = "2022-06-20",
                    overview = "A dramatic story.",
                    posterPath = "/another_film_poster.jpg"
                )
            )
        )

        private val NULL_CAST_RESPONSE = ActorMoviesResponse(
            cast = null
        )

        private val EMPTY_CAST_RESPONSE = ActorMoviesResponse(
            cast = emptyList()
        )

        private val NULL_VALUES_MOVIES_RESPONSE = ActorMoviesResponse(
            cast = listOf(
                ActorMoviesResponse.ActorMovieDto(
                    id = 676L,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                ),
                ActorMoviesResponse.ActorMovieDto(
                    id = 101L,
                    title = "Valid Movie",
                    genreIds = listOf(35L),
                    voteAverage = 6.8,
                    releaseDate = "2021-03-10",
                    overview = "A comedy film.",
                    posterPath = "/comedy_poster.jpg"
                )
            )
        )

        private val EMPTY_STRING_VALUES_RESPONSE = ActorMoviesResponse(
            cast = listOf(
                ActorMoviesResponse.ActorMovieDto(
                    id = 202L,
                    title = "",
                    genreIds = emptyList(),
                    voteAverage = 5.0,
                    releaseDate = "",
                    overview = "",
                    posterPath = ""
                )
            )
        )

        private val NULL_ID_MOVIES_RESPONSE = ActorMoviesResponse(
            cast = listOf(
                ActorMoviesResponse.ActorMovieDto(
                    id = null,
                    title = "Invalid Movie",
                    genreIds = listOf(16L),
                    voteAverage = 7.5,
                    releaseDate = "2023-08-15",
                    overview = "This should be filtered out.",
                    posterPath = "/invalid_poster.jpg"
                ),
                ActorMoviesResponse.ActorMovieDto(
                    id = 303L,
                    title = "Valid Movie",
                    genreIds = listOf(27L),
                    voteAverage = 6.3,
                    releaseDate = "2023-10-31",
                    overview = "A horror film.",
                    posterPath = "/horror_poster.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_MOVIES = listOf(
            MovieDto(
                id = 123L,
                title = "The Great Movie",
                genres = emptyList(),
                imdbRating = 8.5,
                userRating = null,
                releaseDate = "2023-12-15",
                overview = "An amazing movie about adventure.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/great_movie_poster.jpg",
                runtimeMinutes = 0,
                trailerURL = ""
            ),
            MovieDto(
                id = 456L,
                title = "Another Film",
                genres = emptyList(),
                imdbRating = 7.2,
                userRating = null,
                releaseDate = "2022-06-20",
                overview = "A dramatic story.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/another_film_poster.jpg",
                runtimeMinutes = 0,
                trailerURL = ""
            )
        )

        private val EXPECTED_EMPTY_LIST = emptyList<MovieDto>()

        private val EXPECTED_NULL_VALUES_MOVIES = listOf(
            MovieDto(
                id = 676L,
                title = "",
                genres = emptyList(),
                imdbRating = 0.0,
                userRating = null,
                releaseDate = "0001-01-01",
                overview = "",
                posterPictureURL = "",
                runtimeMinutes = 0,
                trailerURL = ""
            ),
            MovieDto(
                id = 101L,
                title = "Valid Movie",
                genres = emptyList(),
                imdbRating = 6.8,
                userRating = null,
                releaseDate = "2021-03-10",
                overview = "A comedy film.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/comedy_poster.jpg",
                runtimeMinutes = 0,
                trailerURL = ""
            )
        )

        private val EXPECTED_EMPTY_STRING_VALUES = listOf(
            MovieDto(
                id = 202L,
                title = "",
                genres = emptyList(),
                imdbRating = 5.0,
                userRating = null,
                releaseDate = "0001-01-01",
                overview = "",
                posterPictureURL = "",
                runtimeMinutes = 0,
                trailerURL = ""
            )
        )

        private val EXPECTED_FILTERED_NULL_ID = listOf(
            MovieDto(
                id = 303L,
                title = "Valid Movie",
                genres = emptyList(),
                imdbRating = 6.3,
                userRating = null,
                releaseDate = "2023-10-31",
                overview = "A horror film.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/horror_poster.jpg",
                runtimeMinutes = 0,
                trailerURL = ""
            )
        )
    }

    @Test
    fun `should convert all valid movies to MovieDto list`() {
        val moviesResponse = COMPLETE_MOVIES_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_MOVIES)
    }

    @Test
    fun `should return empty list when cast is null`() {
        val moviesResponse = NULL_CAST_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when cast is empty`() {
        val moviesResponse = EMPTY_CAST_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should handle null values by using default values`() {
        val moviesResponse = NULL_VALUES_MOVIES_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_MOVIES)
    }

    @Test
    fun `should handle empty string values correctly`() {
        val moviesResponse = EMPTY_STRING_VALUES_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_STRING_VALUES)
    }

    @Test
    fun `should filter out movies with null IDs`() {
        val moviesResponse = NULL_ID_MOVIES_RESPONSE

        val result = moviesResponse.toMovieDtoList()

        assertThat(result).isEqualTo(EXPECTED_FILTERED_NULL_ID)
    }
}
