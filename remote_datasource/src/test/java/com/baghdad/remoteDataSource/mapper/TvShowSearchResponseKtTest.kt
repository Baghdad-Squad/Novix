package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.mapper.search.toPagedTvShowDtos
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowSearchResponseMapperTest {

    private val genres = listOf(
        GenreDto(18L, "Drama", GenreDto.GenreType.TV_SHOW),
        GenreDto(35L, "Comedy", GenreDto.GenreType.TV_SHOW),
        GenreDto(99L, "Documentary", GenreDto.GenreType.TV_SHOW)
    )

    @Test
    fun `should map tv shows correctly when results contain valid data`() {
        // Given
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 2,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 201,
                    title = "Mr. Robot",
                    genreIds = listOf(18, 35),
                    voteAverage = 9.5,
                    releaseDate = "2008-01-20",
                    overview = "Chemistry teacher turned meth producer",
                    posterPath = "/breakingbad.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos(genres)

        // Then
        assertThat(result.data).hasSize(1)
        val tvShow = result.data.first()
        assertThat(tvShow.id).isEqualTo(response.results?.first()?.id)
        assertThat(tvShow.title).isEqualTo(response.results?.first()?.title)
        assertThat(tvShow.genres.map { it.name }).containsExactly("Drama", "Comedy")
        assertThat(tvShow.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500/breakingbad.jpg")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = TvShowSearchResponse(page = 1, totalPages = 1, results = null)

        // When
        val result = response.toPagedTvShowDtos(genres)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip tv shows when id is null`() {
        // Given
        val response = TvShowSearchResponse(
            page = 2,
            totalPages = 4,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = null,
                    title = "Invalid Show",
                    genreIds = listOf(18),
                    voteAverage = 7.0,
                    releaseDate = "2015-09-09",
                    overview = "Should not appear",
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos(genres)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 0,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos(emptyList())

        // Then
        assertThat(result.data).hasSize(1)
        val tvShow = result.data.first()
        assertThat(tvShow.id).isEqualTo(0)
        assertThat(tvShow.title).isEmpty()
        assertThat(tvShow.genres).isEmpty()
        assertThat(tvShow.imdbRating).isEqualTo(0.0)
        assertThat(tvShow.posterPictureURL).isEmpty()
        assertThat(tvShow.numberOfSeasons).isEqualTo(1)
    }

    @Test
    fun `should return matching genres only`() {
        // Given
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 303,
                    title = "Mr. Robot",
                    genreIds = listOf(99, 77),
                    voteAverage = 9.0,
                    releaseDate = "2006-03-05",
                    overview = "CS series",
                    posterPath = "/test.jpg"
                )
            )
        )

        // When
        val result = response.toPagedTvShowDtos(genres)

        // Then
        val tvShow = result.data.first()
        assertThat(tvShow.genres.map { it.name }).containsExactly("Documentary")
    }

    @Test
    fun `should use default values when optional parameters are not provided`() {
        // Given
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                TvShowSearchResponse.Result(
                    id = 303,
                    title = "Mr. Robot",
                    voteAverage = 9.0,
                    releaseDate = "2006-03-05",
                    overview = "CS series",
                    posterPath = "/test.jpg"
                )
            )
        )

        val tvShowDto = response.toPagedTvShowDtos(emptyList()).data.first()

        // Then
        assertThat(tvShowDto.id).isEqualTo(response.results?.first()?.id)
        assertThat(tvShowDto.title).isEqualTo(response.results?.first()?.title)
        assertThat(tvShowDto.genres).isEmpty()
        assertThat(tvShowDto.userRating).isNull()
        assertThat(tvShowDto.numberOfSeasons).isEqualTo(1)
        assertThat(tvShowDto.posterPictureURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500/test.jpg")
    }

    @Test
    fun `should map TvShow with default parameters`() {
        // Given
        val result = TvShowSearchResponse.Result(
            id = 202,
            title = "Default Params Test",
            genreIds = listOf(),
            voteAverage = null,
            releaseDate = null,
            overview = null,
            posterPath = null
        )

        // When
        val response = TvShowSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(result)
        ).toPagedTvShowDtos(emptyList())

        // Then
        val tvShow = response.data.first()
        assertThat(tvShow.userRating).isNull()
        assertThat(tvShow.genres).isEmpty()
        assertThat(tvShow.releaseDate).isEmpty()
    }
}
