package com.baghdad.repository

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.CastMemberDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Locale

class MovieRepositoryImplTest {

    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var remoteMovieDataSource: RemoteMovieDataSource
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        remoteMovieDataSource = mockk()
        movieRepositoryImpl = MovieRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            remoteMovieDataSource = remoteMovieDataSource
        )
    }

    @Test
    fun `getGenres should return list of genres when remote call succeeds`() = runTest {

        val mockGenreDtos = listOf(
            createMockGenreDto(1, "Action"),
            createMockGenreDto(2, "Comedy")
        )
        val expectedGenres = listOf(
            createMockGenre(1, "Action"),
            createMockGenre(2, "Comedy")
        )

        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns mockGenreDtos

        val result = movieRepositoryImpl.getGenres()

        assertEquals(expectedGenres.size, result.size)
        assertEquals(expectedGenres[0].id, result[0].id)
        assertEquals(expectedGenres[0].name, result[0].name)
        assertEquals(expectedGenres[1].id, result[1].id)
        assertEquals(expectedGenres[1].name, result[1].name)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should return empty list when no genres found`() = runTest {

        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } returns emptyList()

        val result = movieRepositoryImpl.getGenres()

        assertEquals(emptyList<Genre>(), result)
        coVerify { remoteGenreDataSource.getMovieGenre(language = Locale.getDefault().language) }
    }

    @Test
    fun `getGenres should throw exception when remote call fails`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { remoteGenreDataSource.getMovieGenre(language = any()) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getGenres()
        }
    }

    @Test
    fun `getSimilarMovies should return list of movies when remote call succeeds`() = runTest {

        val movieId = 123L
        val mockMovieDtos = listOf(createMockMovieDto())
        val expectedMovies = listOf(createMockMovie())

        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns mockMovieDtos

        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        assertEquals(expectedMovies.size, result.size)
        val movie = result[0]
        assertEquals(456L, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals(1, movie.genres.size)
        assertEquals(8.0, movie.averageRating)
        assertEquals(7.5, movie.userRating)
        assertEquals(LocalDate.parse("2023-01-01"), movie.releaseDate)
        assertEquals("Test movie overview", movie.overview)
        assertEquals("/movie_poster.jpg", movie.posterImageURL)
        assertEquals(120, movie.runtimeMinutes)
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should return empty list when no similar movies found`() = runTest {

        val movieId = 123L
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getSimilarMovies(movieId)

        assertEquals(emptyList<Movie>(), result)
        coVerify { remoteMovieDataSource.getSimilarMovies(movieId) }
    }

    @Test
    fun `getSimilarMovies should throw exception when remote call fails`() = runTest {

        val movieId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getSimilarMovies(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getSimilarMovies(movieId)
        }
    }

    @Test
    fun `getMovieDetails should return movie when remote call succeeds`() = runTest {

        val movieId = 456L
        val mockMovieDto = createMockMovieDto()
        val expectedMovie = createMockMovie()

        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } returns mockMovieDto

        val result = movieRepositoryImpl.getMovieDetails(movieId)

        assertEquals(expectedMovie.id, result.id)
        assertEquals(expectedMovie.title, result.title)
        assertEquals(expectedMovie.genres.size, result.genres.size)
        assertEquals(expectedMovie.averageRating, result.averageRating)
        assertEquals(expectedMovie.userRating, result.userRating)
        assertEquals(expectedMovie.releaseDate, result.releaseDate)
        assertEquals(expectedMovie.overview, result.overview)
        assertEquals(expectedMovie.posterImageURL, result.posterImageURL)
        assertEquals(expectedMovie.runtimeMinutes, result.runtimeMinutes)
        coVerify { remoteMovieDataSource.getMovieDetails(movieId) }
    }

    @Test
    fun `getMovieDetails should throw exception when remote call fails`() = runTest {

        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieDetails(movieId)
        }
    }

    @Test
    fun `getMovieCastMembers should return list of cast members when remote call succeeds`() = runTest {

        val movieId = 456L
        val mockCastMemberDtos = listOf(
            createMockCastMemberDto(1L, "John Doe", "Main Character"),
            createMockCastMemberDto(2L, "Jane Smith", "Supporting Character")
        )
        val expectedCastMembers = listOf(
            createMockCastMember(1L, "John Doe", "Main Character"),
            createMockCastMember(2L, "Jane Smith", "Supporting Character")
        )

        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns mockCastMemberDtos

        val result = movieRepositoryImpl.getMovieCastMembers(movieId)

        assertEquals(expectedCastMembers.size, result.size)
        assertEquals(expectedCastMembers[0].actor.id, result[0].actor.id)
        assertEquals(expectedCastMembers[0].actor.name, result[0].actor.name)
        assertEquals(expectedCastMembers[0].characterName, result[0].characterName)

        assertEquals(expectedCastMembers[1].actor.id, result[1].actor.id)
        assertEquals(expectedCastMembers[1].actor.name, result[1].actor.name)
        assertEquals(expectedCastMembers[1].characterName, result[1].characterName)

        coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    @Test
    fun `getMovieCastMembers should return empty list when no cast members found`() = runTest {

        val movieId = 456L
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } returns emptyList()

        val result = movieRepositoryImpl.getMovieCastMembers(movieId)

        assertEquals(emptyList<CastMember>(), result)
        coVerify { remoteMovieDataSource.getMovieCastMembers(movieId) }
    }

    @Test
    fun `getMovieCastMembers should throw exception when remote call fails`() = runTest {

        val movieId = 456L
        val exception = RuntimeException("Network error")
        coEvery { remoteMovieDataSource.getMovieCastMembers(movieId) } throws exception

        assertThrows<Exception> {
            movieRepositoryImpl.getMovieCastMembers(movieId)
        }
    }

    companion object {

        private fun createMockGenreDto(id: Long, name: String) = GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.MOVIE
        )

        private fun createMockMovieDto() = MovieDto(
            id = 456L,
            title = "Test Movie",
            genres = listOf(GenreDto(28, "Action", type = GenreDto.GenreType.MOVIE)),
            imdbRating = 8.0,
            userRating = 7.5,
            releaseDate = "2023-01-01",
            overview = "Test movie overview",
            posterPictureURL = "/movie_poster.jpg",
            runtimeMinutes = 120
        )

        private fun createMockCastMemberDto(
            id: Long = 1L,
            name: String = "Johnny Deep",
            character: String = "Jack Sparrow"
        ): CastMemberDto {
            val actor = ActorDto(
                id = id,
                name = name,
                imageUrl = "/profile_$id.jpg",
                biography = "Test biography for $name",
                birthdayDate = "1970-01-01",
                deathDate = null,
                placeOfBirth = "USA",
                headerPictures = listOf("/header_1.jpg", "/header_2.jpg"),
                department = "Acting"
            )

            return CastMemberDto(
                actor = actor,
                characterName = character
            )
        }

        private fun createMockGenre(
            id: Long,
            name: String
        ): Genre {
            return Genre(
                id = id,
                name = name
            )
        }

        private fun createMockMovie() = Movie(
            id = 456L,
            title = "Test Movie",
            genres = listOf(Genre(28, "Action")),
            averageRating = 8.0,
            userRating = 7.5,
            releaseDate = LocalDate.parse("2023-01-01"),
            overview = "Test movie overview",
            posterImageURL = "/movie_poster.jpg",
            runtimeMinutes = 120
        )

        private fun createMockCastMember(
            id: Long,
            name: String,
            character: String
        ): CastMember {
            val actor = Actor(
                id = id,
                name = name,
                profilePictureURL = "/profile_$id.jpg",
                birthDate = LocalDate.parse("1985-06-09"),
                placeOfBirth = "USA",
                deathDate = null,
                biography = "An experienced actor known for various roles.",
                headerPictures = listOf("/header_1.jpg", "/header_2.jpg"),
                department = "Acting"
            )

            return CastMember(
                actor = actor,
                characterName = character
            )
        }
}   }