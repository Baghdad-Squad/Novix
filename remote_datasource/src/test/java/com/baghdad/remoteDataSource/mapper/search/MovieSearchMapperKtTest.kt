package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieSearchResponseMapperTest {

    private val genres = listOf(
        GenreDto(28L, "Action", GenreDto.GenreType.MOVIE),
        GenreDto(12L, "Adventure", GenreDto.GenreType.MOVIE),
        GenreDto(35L, "Comedy", GenreDto.GenreType.MOVIE)
    )

    @Test
    fun `should map movies correctly when results contain valid data`() {
        // Given
        val response = MovieSearchResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                MovieSearchResponse.Result(
                    id = 101,
                    title = "Inception",
                    genreIds = listOf(28, 12),
                    voteAverage = 8.8,
                    releaseDate = "2010-07-16",
                    overview = "Mind-bending thriller",
                    posterPath = "/inception.jpg"
                )
            )
        )

        // When
        val result = response.toPagedMovieDtos(genres)

        // Then
        assertThat(result.data).hasSize(1)
        val movie = result.data.first()
        assertThat(movie.id).isEqualTo(response.results?.first()?.id)
        assertThat(movie.title).isEqualTo(response.results?.first()?.title)
        assertThat(movie.genres.map { it.name }).containsExactly("Action", "Adventure")
        assertThat(movie.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500/inception.jpg")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = MovieSearchResponse(page = 1, totalPages = 1, results = null)

        // When
        val result = response.toPagedMovieDtos(genres)

        // Then
        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should skip movies when id is null`() {
        // Given
        val response = MovieSearchResponse(
            page = 2,
            totalPages = 4,
            results = listOf(
                MovieSearchResponse.Result(
                    id = null,
                    title = "Invalid Movie",
                    genreIds = listOf(28),
                    voteAverage = 7.5,
                    releaseDate = "2020-01-01",
                    overview = "Should be skipped",
                    posterPath = "/invalid.jpg"
                )
            )
        )

        // When
        val result = response.toPagedMovieDtos(genres)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `should use defaults when fields are null`() {
        // Given
        val response = MovieSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                MovieSearchResponse.Result(
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
        val result = response.toPagedMovieDtos(emptyList())

        // Then
        assertThat(result.data).hasSize(1)
        val movie = result.data.first()
        assertThat(movie.id).isEqualTo(0)
        assertThat(movie.title).isEmpty()
        assertThat(movie.genres).isEmpty()
        assertThat(movie.imdbRating).isEqualTo(0.0)
        assertThat(movie.posterPictureURL).isEmpty()
    }

    @Test
    fun `should return matching genres only`() {
        // Given
        val response = MovieSearchResponse(
            page = 1,
            totalPages = 1,
            results = listOf(
                MovieSearchResponse.Result(
                    id = 55,
                    title = "Comedy Flick",
                    genreIds = listOf(35, 99),
                    voteAverage = 6.5,
                    releaseDate = "2018-07-07",
                    overview = "Funny movie",
                    posterPath = "/comedy.jpg"
                )
            )
        )

        // When
        val result = response.toPagedMovieDtos(genres)

        // Then
        val movie = result.data.first()
        assertThat(movie.genres.map { it.name })
            .containsExactly(result.data.first().genres.first().name)
    }

    @Test
    fun `should use default empty genres when parameter not provided`() {
        // Given
        val result = MovieSearchResponse.Result(
            id = 42,
            title = "Default Genres Test",
            genreIds = listOf(18, 35),
            voteAverage = 7.8,
            releaseDate = "2021-08-01",
            overview = "Testing default genres",
            posterPath = "/default.jpg"
        )

        // When
        val movieDto = result.toMovieDto()

        // Then
        assertThat(movieDto.id).isEqualTo(result.id)
        assertThat(movieDto.genres).isEmpty()
        assertThat(movieDto.title).isEqualTo(result.title)
        assertThat(movieDto.posterPictureURL).isEqualTo("https://image.tmdb.org/t/p/w500${result.posterPath}")
    }
}
