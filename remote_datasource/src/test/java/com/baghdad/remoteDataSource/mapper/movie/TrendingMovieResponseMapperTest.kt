package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse.Result
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingMovieResponseMapperTest {

    companion object {
        private val COMPLETE_RESPONSE = TrendingMovieResponse(
            page = 1,
            totalPages = 10,
            results = listOf(
                Result(
                    id = 123L,
                    title = "Inception",
                    posterPath = "/inception.jpg",
                    backdropPath = "/inception_bg.jpg",
                    overview = "A thief who steals corporate secrets...",
                    releaseDate = "2010-07-16",
                    voteAverage = 8.4,
                    popularity = 85.5,
                    genreIds = listOf(28L, 878L)
                )
            )
        )

        private val EXPECTED_COMPLETE_DTO = PagedResultDto(
            data = listOf(
                MovieDto(
                    id = 123L,
                    title = "Inception",
                    posterPictureURL = "https://image.tmdb.org/t/p/w500/inception.jpg",
                    trailerURL = "https://image.tmdb.org/t/p/w500/inception_bg.jpg",
                    overview = "A thief who steals corporate secrets...",
                    releaseDate = "2010-07-16",
                    imdbRating = 8.4,
                    runtimeMinutes = 0,
                    userRating = 85.5,
                    genres = listOf(
                        GenreDto(28L, "", GenreDto.GenreType.MOVIE),
                        GenreDto(878L, "", GenreDto.GenreType.MOVIE)
                    )
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val NULL_VALUES_RESPONSE = TrendingMovieResponse(
            page = null,
            totalPages = null,
            results = null
        )

        private val EXPECTED_NULL_VALUES_DTO = PagedResultDto<MovieDto>(
            data = emptyList(),
            nextKey = null,
            prevKey = null
        )

        private val FIRST_PAGE_RESPONSE = TrendingMovieResponse(
            page = 1,
            totalPages = 5,
            results = emptyList()
        )

        private val EXPECTED_FIRST_PAGE_DTO = PagedResultDto<MovieDto>(
            data = emptyList(),
            nextKey = 2,
            prevKey = null
        )

        private val LAST_PAGE_RESPONSE = TrendingMovieResponse(
            page = 5,
            totalPages = 5,
            results = emptyList()
        )

        private val EXPECTED_LAST_PAGE_DTO = PagedResultDto<MovieDto>(
            data = emptyList(),
            nextKey = null,
            prevKey = 4
        )

        private val MIDDLE_PAGE_RESPONSE = TrendingMovieResponse(
            page = 3,
            totalPages = 5,
            results = emptyList()
        )

        private val EXPECTED_MIDDLE_PAGE_DTO = PagedResultDto<MovieDto>(
            data = emptyList(),
            nextKey = 4,
            prevKey = 2
        )
    }

    @Test
    fun `should convert complete TrendingMovieResponse to PagedResultDto`() {
        val result = COMPLETE_RESPONSE.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle null values in TrendingMovieResponse`() {
        val result = NULL_VALUES_RESPONSE.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should set correct pagination keys for first page`() {
        val result = FIRST_PAGE_RESPONSE.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_FIRST_PAGE_DTO)
    }

    @Test
    fun `should set correct pagination keys for last page`() {
        val result = LAST_PAGE_RESPONSE.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_LAST_PAGE_DTO)
    }

    @Test
    fun `should set correct pagination keys for middle page`() {
        val result = MIDDLE_PAGE_RESPONSE.toPagedMovieDtos()

        assertThat(result).isEqualTo(EXPECTED_MIDDLE_PAGE_DTO)
    }

    @Test
    fun `should filter out results with null id`() {
        val response = TrendingMovieResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                Result(id = 1L, title = "Valid"),
                Result(id = null, title = "Invalid"),
                Result(id = 2L, title = "Valid")
            )
        )

        val result = response.toPagedMovieDtos()

        assertThat(result.data.map { it.title }).containsExactly("Valid", "Valid")
    }
}