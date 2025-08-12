package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieDetailsResponseMapperTest {

    companion object {
        private const val VALID_ID = 1L
        private const val TITLE = "Inception"
        private const val OVERVIEW = "A mind-bending thriller"
        private const val RELEASE_DATE = "2010-07-16"
        private const val POSTER_PATH = "/inception.jpg"
        private const val RUNTIME = 148
        private const val IMDB_RATING = 8.8
        private const val USER_RATING = 7.5
        private const val GENRE_ID_1 = 28L
        private const val GENRE_NAME_1 = "Action"
        private const val GENRE_ID_2 = 878L
        private const val GENRE_NAME_2 = "Sci-Fi"

        private val GENRES = listOf(
            MovieDetailsResponse.Genre(GENRE_ID_1, GENRE_NAME_1),
            MovieDetailsResponse.Genre(GENRE_ID_2, GENRE_NAME_2)
        )

        private val EXPECTED_GENRE_DTOS = listOf(
            GenreDto(GENRE_ID_1, GENRE_NAME_1, GenreDto.GenreType.MOVIE),
            GenreDto(GENRE_ID_2, GENRE_NAME_2, GenreDto.GenreType.MOVIE)
        )

        private val FULL_RESPONSE = MovieDetailsResponse(
            id = VALID_ID,
            title = TITLE,
            genres = GENRES,
            voteAverage = IMDB_RATING,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH,
            runtime = RUNTIME
        )
    }

    @Test
    fun `toDto should map all fields correctly`() {
        val result = FULL_RESPONSE.toDto(USER_RATING)

        assertThat(result).isEqualTo(
            MovieDto(
                id = VALID_ID,
                title = TITLE,
                genres = EXPECTED_GENRE_DTOS,
                imdbRating = IMDB_RATING,
                userRating = USER_RATING,
                releaseDate = RELEASE_DATE,
                overview = OVERVIEW,
                posterPictureURL = getImageUrlFromPath(POSTER_PATH),
                runtimeMinutes = RUNTIME,
                trailerURL = ""
            )
        )
    }

    @Test
    fun `toDto should fallback to -1L and empty strings for null fields`() {
        val response = MovieDetailsResponse(
            id = null,
            title = null,
            genres = null,
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null,
            runtime = null
        )

        val result = response.toDto()

        assertThat(result).isEqualTo(
            MovieDto(
                id = -1L,
                title = "",
                genres = emptyList(),
                imdbRating = 0.0,
                userRating = null,
                releaseDate = "",
                overview = "",
                posterPictureURL = getImageUrlFromPath(null),
                runtimeMinutes = 0,
                trailerURL = ""
            )
        )
    }

    @Test
    fun `toDto should skip null genres`() {
        val response = FULL_RESPONSE.copy(
            genres = listOf(
                MovieDetailsResponse.Genre(GENRE_ID_1, GENRE_NAME_1),
                MovieDetailsResponse.Genre(null, null) // should be filtered out
            )
        )

        val result = response.toDto()

        assertThat(result.genres).containsExactly(
            GenreDto(GENRE_ID_1, GENRE_NAME_1, GenreDto.GenreType.MOVIE)
        )
    }
}