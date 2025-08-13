package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowSearchMapperTest {

    companion object {
        private val TEST_GENRES = listOf(
            GenreDto(10759, "Action & Adventure", GenreDto.GenreType.TV_SHOW),
            GenreDto(18, "Drama", GenreDto.GenreType.TV_SHOW),
            GenreDto(35, "Comedy", GenreDto.GenreType.TV_SHOW)
        )

        private val COMPLETE_RESPONSE = TvShowSearchResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 123L,
                    title = "Breaking Bad",
                    genreIds = listOf(18L),
                    voteAverage = 8.9,
                    releaseDate = "2008-01-20",
                    overview = "A high school chemistry teacher diagnosed with cancer...",
                    posterPath = "/breakingbad.jpg"
                ),
                TvShowSearchResponse.Result(
                    id = 456L,
                    title = "The Mandalorian",
                    genreIds = listOf(10759L, 18L),
                    voteAverage = 8.5,
                    releaseDate = "2019-11-12",
                    overview = "After the fall of the Galactic Empire...",
                    posterPath = "/mandalorian.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_DTO = PagedResultDto(
            data = listOf(
                TvShowDto(
                    id = 123L,
                    title = "Breaking Bad",
                    genres = listOf(
                        GenreDto(18, "Drama", GenreDto.GenreType.TV_SHOW)
                    ),
                    imdbRating = 8.9,
                    userRating = 0,
                    releaseDate = "2008-01-20",
                    overview = "A high school chemistry teacher diagnosed with cancer...",
                    posterPictureURL = "https://image.tmdb.org/t/p/w500/breakingbad.jpg",
                    numberOfSeasons = 1,
                    trailerURL = "",
                    headerImagesURLs = emptyList()
                ),
                TvShowDto(
                    id = 456L,
                    title = "The Mandalorian",
                    genres = listOf(
                        GenreDto(10759, "Action & Adventure", GenreDto.GenreType.TV_SHOW),
                        GenreDto(18, "Drama", GenreDto.GenreType.TV_SHOW)
                    ),
                    imdbRating = 8.5,
                    userRating = 0,
                    releaseDate = "2019-11-12",
                    overview = "After the fall of the Galactic Empire...",
                    posterPictureURL = "https://image.tmdb.org/t/p/w500/mandalorian.jpg",
                    numberOfSeasons = 1,
                    trailerURL = "",
                    headerImagesURLs = emptyList()
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val NULL_VALUES_RESPONSE = TvShowSearchResponse(
            page = null,
            totalPages = null,
            results = listOf(
                TvShowSearchResponse.Result(
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

        private val MIXED_RESPONSE = TvShowSearchResponse(
            page = 2,
            totalPages = 3,
            results = listOf(
                null,
                TvShowSearchResponse.Result(
                    id = 789L,
                    title = "Stranger Things",
                    genreIds = listOf(18L, 999L), // 999 not in TEST_GENRES
                    voteAverage = 8.7,
                    releaseDate = "2016-07-15",
                    overview = "When a young boy vanishes...",
                    posterPath = "/strangerthings.jpg"
                ),
                TvShowSearchResponse.Result(
                    id = null,
                    title = "Invalid Show"
                )
            )
        )
    }

    @Test
    fun `should convert complete TvShowSearchResponse to PagedResultDto with genres`() {
        val result = COMPLETE_RESPONSE.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should filter out results with null id`() {
        val result = NULL_VALUES_RESPONSE.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should filter out null results and results with null id`() {
        val result = MIXED_RESPONSE.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].title).isEqualTo("Stranger Things")
        assertThat(result.data[0].genres).containsExactly(
            GenreDto(18, "Drama", GenreDto.GenreType.TV_SHOW)
        )
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `should handle empty results list`() {
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = emptyList()
        )
        val result = response.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should handle null results`() {
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = null
        )
        val result = response.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should filter only matching TV genres`() {
        val response = TvShowSearchResponse(
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 999L,
                    title = "Test Show",
                    genreIds = listOf(18L, 28L) // 28 is movie genre
                )
            )
        )
        val result = response.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data[0].genres).containsExactly(
            GenreDto(18, "Drama", GenreDto.GenreType.TV_SHOW)
        )
    }

    @Test
    fun `should handle null genreIds`() {
        val response = TvShowSearchResponse(
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 111L,
                    title = "No Genre Show",
                    genreIds = null
                )
            )
        )
        val result = response.toPagedTvShowDtos(TEST_GENRES)

        assertThat(result.data[0].genres).isEmpty()
    }
}
