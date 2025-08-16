package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MyRatingsMoviesMapperTest {

    companion object {
        private val COMPLETE_MOVIE_RESPONSE = MyRatingMoviesResponse(
            page = 1,
            results = listOf(
                MyRatingMoviesResponse.MovieItem(
                    id = 101L,
                    title = "Inception",
                    genreIds = listOf(1L, 2L),
                    voteAverage = 8.8,
                    rating = 9,
                    releaseDate = "2010-07-16",
                    overview = "A mind-bending thriller.",
                    posterPath = "/poster.jpg"
                )
            ),
            totalPages = 3,
            totalResults = 1
        )

        private val EXPECTED_COMPLETE_DTO = PagedResultDto(
            data = listOf(
                MovieDto(
                    id = 101L,
                    title = "Inception",
                    genres = listOf(
                        GenreDto(id = 1L, name = "", type = GenreDto.GenreType.MOVIE),
                        GenreDto(id = 2L, name = "", type = GenreDto.GenreType.MOVIE)
                    ),
                    imdbRating = 8.8,
                    userRating = 9.0,
                    releaseDate = "2010-07-16",
                    overview = "A mind-bending thriller.",
                    posterPictureURL = "https://image.tmdb.org/t/p/w500/poster.jpg",
                    trailerURL = "",
                    runtimeMinutes = 0
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val NULL_VALUES_MOVIE_RESPONSE = MyRatingMoviesResponse(
            page = null,
            results = listOf(
                MyRatingMoviesResponse.MovieItem(
                    id = null,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    rating = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            ),
            totalPages = null,
            totalResults = null
        )

        private val EXPECTED_NULL_VALUES_DTO = PagedResultDto<MovieDto>(
            data = emptyList(),
            nextKey = null,
            prevKey = null
        )

        private val MIXED_NULL_MOVIE_RESPONSE = MyRatingMoviesResponse(
            page = 5,
            results = listOf(
                MyRatingMoviesResponse.MovieItem(
                    id = 202L,
                    title = null,
                    genreIds = listOf(),
                    voteAverage = null,
                    rating = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            ),
            totalPages = 5,
            totalResults = 1
        )

        private val EXPECTED_MIXED_NULL_DTO = PagedResultDto(
            data = listOf(
                MovieDto(
                    id = 202L,
                    title = "",
                    genres = emptyList(),
                    imdbRating = 0.0,
                    userRating = null,
                    releaseDate = "0001-01-01",
                    overview = "",
                    posterPictureURL = "",
                    trailerURL = "",
                    runtimeMinutes = 0
                )
            ),
            nextKey = null,
            prevKey = 4
        )
    }

    @Test
    fun `should convert complete MyRatingMoviesResponse to PagedResultDto`() {
        val movieResponse = COMPLETE_MOVIE_RESPONSE

        val result = movieResponse.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle all null values by using default values`() {
        val movieResponse = NULL_VALUES_MOVIE_RESPONSE

        val result = movieResponse.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should handle mixed null and non-null values correctly`() {
        val movieResponse = MIXED_NULL_MOVIE_RESPONSE

        val result = movieResponse.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_MIXED_NULL_DTO)
    }
}