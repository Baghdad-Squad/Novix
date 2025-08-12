package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieSearchResponseMapperTest {

    companion object {
        private val TEST_GENRES = listOf(
            GenreDto(28, "Action", GenreDto.GenreType.MOVIE),
            GenreDto(12, "Adventure", GenreDto.GenreType.MOVIE),
            GenreDto(16, "Animation", GenreDto.GenreType.MOVIE)
        )

        private val NULL_VALUES_RESPONSE = MovieSearchResponse(
            page = null,
            totalPages = null,
            results = listOf(
                MovieSearchResponse.Result(
                    id = null,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            )
        )

        private val MIXED_RESPONSE = MovieSearchResponse(
            page = 2,
            totalPages = 3,
            results = listOf(
                null,
                MovieSearchResponse.Result(
                    id = 789L,
                    title = "Inception",
                    genreIds = listOf(28L, 878L), // 878 not in TEST_GENRES
                    voteAverage = 8.8,
                    releaseDate = "2010-07-16",
                    overview = "A thief who steals corporate secrets...",
                    posterPath = "/inception.jpg"
                ),
                MovieSearchResponse.Result(
                    id = null,
                    title = "Invalid Movie"
                )
            )
        )
    }

    @Test
    fun `should filter out results with null id`() {
        val result = NULL_VALUES_RESPONSE.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should filter out null results and results with null id`() {
        val result = MIXED_RESPONSE.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("Inception")
        assertThat(result.data[0].genres).containsExactly(
            GenreDto(28, "Action", GenreDto.GenreType.MOVIE)
        )
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `should handle empty results list`() {
        val response = MovieSearchResponse(
            page = 1,
            totalPages = 1,
            results = emptyList()
        )
        val result = response.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should handle null results`() {
        val response = MovieSearchResponse(
            page = 1,
            totalPages = 1,
            results = null
        )
        val result = response.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should filter only matching genres`() {
        val response = MovieSearchResponse(
            results = listOf(
                MovieSearchResponse.Result(
                    id = 999L,
                    title = "Test Movie",
                    genreIds = listOf(28L, 999L) // 999 not in TEST_GENRES
                )
            )
        )
        val result = response.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data[0].genres).containsExactly(
            GenreDto(28, "Action", GenreDto.GenreType.MOVIE)
        )
    }

    @Test
    fun `should handle null genreIds`() {
        val response = MovieSearchResponse(
            results = listOf(
                MovieSearchResponse.Result(
                    id = 111L,
                    title = "No Genre Movie",
                    genreIds = null
                )
            )
        )
        val result = response.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data[0].genres).isEmpty()
    }

    @Test
    fun `should handle empty genreIds`() {
        val response = MovieSearchResponse(
            results = listOf(
                MovieSearchResponse.Result(
                    id = 222L,
                    title = "Empty Genre Movie",
                    genreIds = emptyList()
                )
            )
        )
        val result = response.toPagedMovieDtos(TEST_GENRES)

        assertThat(result.data[0].genres).isEmpty()
    }
}
