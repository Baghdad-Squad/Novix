package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DiscoverMovieMapperTest {

    companion object {
        private const val VALID_ID = 1L
        private const val GENRE_ID_1 = 101L
        private const val GENRE_ID_2 = 102L
        private const val TITLE = "Movie Title"
        private const val OVERVIEW = "Some overview"
        private const val RELEASE_DATE = "2023-05-01"
        private const val VOTE_AVERAGE = 8.5
        private const val POSTER_PATH = "/poster.jpg"

        private val VALID_RESULT = DiscoverMovieResponse.Result(
            id = VALID_ID,
            title = TITLE,
            genreIds = listOf(GENRE_ID_1, GENRE_ID_2),
            voteAverage = VOTE_AVERAGE,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPath = POSTER_PATH
        )

        private val EXPECTED_MOVIE_DTO = MovieDto(
            id = VALID_ID,
            title = TITLE,
            genres = listOf(
                GenreDto(GENRE_ID_1, "", GenreDto.GenreType.MOVIE),
                GenreDto(GENRE_ID_2, "", GenreDto.GenreType.MOVIE)
            ),
            imdbRating = VOTE_AVERAGE,
            userRating = 0.0,
            releaseDate = RELEASE_DATE,
            overview = OVERVIEW,
            posterPictureURL = getImageUrlFromPath(POSTER_PATH),
            runtimeMinutes = 0,
            trailerURL = ""
        )
    }

    @Test
    fun `toMovieDtos should map valid results`() {
        val response = DiscoverMovieResponse(
            results = listOf(VALID_RESULT)
        )

        val result = response.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_MOVIE_DTO)
    }

    @Test
    fun `toMovieDtos should skip null or invalid ids`() {
        val response = DiscoverMovieResponse(
            results = listOf(
                VALID_RESULT,
                null,
                VALID_RESULT.copy(id = null) // should be filtered out
            )
        )

        val result = response.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_MOVIE_DTO)
    }

    @Test
    fun `toPagedMovieDtos should return PagedResultDto with correct keys`() {
        val response = DiscoverMovieResponse(
            page = 1,
            totalPages = 3,
            results = listOf(VALID_RESULT)
        )

        val result = response.toPagedMovieDtos()

        assertThat(result).isEqualTo(
            PagedResultDto(
                data = listOf(EXPECTED_MOVIE_DTO),
                nextKey = getNextKey(1, 3),
                prevKey = getPreviousKey(1)
            )
        )
    }

    @Test
    fun `toMovieDtos should fallback releaseDate when null or empty`() {
        val response = DiscoverMovieResponse(
            results = listOf(VALID_RESULT.copy(releaseDate = null))
        )

        val result = response.toMovieDtos()

        assertThat(result.first().releaseDate).isEqualTo("0001-01-01")
    }
}