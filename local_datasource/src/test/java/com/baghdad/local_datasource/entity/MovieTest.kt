package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

class MovieTest {

    @Test
    fun `toLocalDto converts MovieDto to Movie with genre IDs`() {
        // Given
        val movieDto = MovieDto(
            id = 101L,
            title = "Test Movie",
            genres = listOf(genreDto1, genreDto2),
            imdbRating = 7.5,
            userRating = 4.2,
            releaseDate = "2023-01-01",
            overview = "Test overview",
            posterPictureURL = "poster.jpg",
            runtimeMinutes = 120,
            trailerURL = "trailer.mp4"
        )

        // When
        val result = movieDto.toLocalDto()

        // Then
        assertThat(result).isEqualTo(
            Movie(
                id = 101L,
                title = "Test Movie",
                genres = listOf(1L, 2L),
                imdbRating = 7.5,
                userRating = 4.2,
                releaseDate = "2023-01-01",
                overview = "Test overview",
                posterPictureURL = "poster.jpg",
                runtimeMinutes = 120
            )
        )
    }

    @Test
    fun `toDto converts Movie to MovieDto with genres`() {
        // Given
        val movie = Movie(
            id = 102L,
            title = "Another Movie",
            genres = listOf(1L, 2L),
            imdbRating = 8.0,
            userRating = 4.5,
            releaseDate = "2023-02-01",
            overview = "Another overview",
            posterPictureURL = "another_poster.jpg",
            runtimeMinutes = 90
        )

        // When
        val result = movie.toDto(listOf(genreDto1, genreDto2))

        // Then
        assertThat(result).isEqualTo(
            MovieDto(
                id = 102L,
                title = "Another Movie",
                genres = listOf(genreDto1, genreDto2),
                imdbRating = 8.0,
                userRating = 4.5,
                releaseDate = "2023-02-01",
                overview = "Another overview",
                posterPictureURL = "another_poster.jpg",
                runtimeMinutes = 90,
                trailerURL = ""
            )
        )
    }

    @Test
    fun `toDtos converts list of Movies with genre mapping`() {
        // Given
        val movies = listOf(
            Movie(
                id = 103L,
                title = "Movie 1",
                genres = listOf(1L),
                imdbRating = 7.0,
                userRating = 3.5,
                releaseDate = "2023-03-01",
                overview = "Overview 1",
                posterPictureURL = "movie1.jpg",
                runtimeMinutes = 100
            ),
            Movie(
                id = 104L,
                title = "Movie 2",
                genres = listOf(2L),
                imdbRating = 8.5,
                userRating = 4.8,
                releaseDate = "2023-04-01",
                overview = "Overview 2",
                posterPictureURL = "movie2.jpg",
                runtimeMinutes = 110
            )
        )

        val genresMap = mapOf(
            1L to genre1,
            2L to genre2
        )

        // When
        val result = movies.toDtos(genresMap)

        // Then
        assertThat(result).containsExactly(
            MovieDto(
                id = 103L,
                title = "Movie 1",
                genres = listOf(genre1.toDto()),
                imdbRating = 7.0,
                userRating = 3.5,
                releaseDate = "2023-03-01",
                overview = "Overview 1",
                posterPictureURL = "movie1.jpg",
                runtimeMinutes = 100,
                trailerURL = ""
            ),
            MovieDto(
                id = 104L,
                title = "Movie 2",
                genres = listOf(genre2.toDto()),
                imdbRating = 8.5,
                userRating = 4.8,
                releaseDate = "2023-04-01",
                overview = "Overview 2",
                posterPictureURL = "movie2.jpg",
                runtimeMinutes = 110,
                trailerURL = ""
            )
        ).inOrder()
    }

    @Test
    fun `toLocalDto handles empty genres list`() {
        // Given
        val movieDto = MovieDto(
            id = 105L,
            title = "No Genres",
            genres = emptyList(),
            imdbRating = 6.0,
            userRating = 3.0,
            releaseDate = "2023-05-01",
            overview = "No genres movie",
            posterPictureURL = "no_genres.jpg",
            runtimeMinutes = 80,
            trailerURL = ""
        )

        // When
        val result = movieDto.toLocalDto()

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `toDto handles empty genres list`() {
        // Given
        val movie = Movie(
            id = 106L,
            title = "Empty Genres",
            genres = emptyList(),
            imdbRating = 5.5,
            userRating = 2.5,
            releaseDate = "2023-06-01",
            overview = "Empty genres movie",
            posterPictureURL = "empty_genres.jpg",
            runtimeMinutes = 70
        )

        // When
        val result = movie.toDto(emptyList())

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `toDtos handles missing genres gracefully`() {
        // Given
        val movies = listOf(
            Movie(
                id = 107L,
                title = "Missing Genre",
                genres = listOf(1L, 99L), // 99L is missing
                imdbRating = 6.5,
                userRating = 3.8,
                releaseDate = "2023-07-01",
                overview = "Missing genre movie",
                posterPictureURL = "missing.jpg",
                runtimeMinutes = 95
            )
        )

        val genresMap = mapOf(1L to genre1)

        // When
        val result = movies.toDtos(genresMap)

        // Then
        assertThat(result[0].genres).containsExactly(genre1.toDto())
    }

    @Test
    fun `toLocalDto preserves all non-genre fields exactly`() {
        // Given
        val movieDto = MovieDto(
            id = 108L,
            title = "Field Test",
            genres = listOf(genreDto1),
            imdbRating = 9.0,
            userRating = 5.0,
            releaseDate = "2023-08-01",
            overview = "Field preservation test",
            posterPictureURL = "field_test.jpg",
            runtimeMinutes = 150,
            trailerURL = "test.mp4"
        )

        // When
        val result = movieDto.toLocalDto()

        // Then
        assertThat(result.id).isEqualTo(108L)
        assertThat(result.title).isEqualTo("Field Test")
        assertThat(result.imdbRating).isWithin(0.001).of(9.0)
        assertThat(result.userRating).isWithin(0.001).of(5.0)
        assertThat(result.releaseDate).isEqualTo("2023-08-01")
        assertThat(result.overview).isEqualTo("Field preservation test")
        assertThat(result.posterPictureURL).isEqualTo("field_test.jpg")
        assertThat(result.runtimeMinutes).isEqualTo(150)
    }

    @Test
    fun `toDto sets empty trailerURL`() {
        // Given
        val movie = Movie(
            id = 109L,
            title = "Trailer Test",
            genres = listOf(1L),
            imdbRating = 7.8,
            userRating = 4.3,
            releaseDate = "2023-09-01",
            overview = "Trailer test",
            posterPictureURL = "trailer_test.jpg",
            runtimeMinutes = 105
        )

        // When
        val result = movie.toDto(listOf(genreDto1))

        // Then
        assertThat(result.trailerURL).isEmpty()
    }

    private val genreDto1 = GenreDto(1L, "Action", GenreDto.GenreType.MOVIE)
    private val genreDto2 = GenreDto(2L, "Comedy", GenreDto.GenreType.MOVIE)
    private val genre1 = Genre(1L, "Action", "MOVIE")
    private val genre2 = Genre(2L, "Comedy", "MOVIE")

}