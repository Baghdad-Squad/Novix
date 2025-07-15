package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.MovieDao
import com.baghdad.local_datasource.database.dto.LocalMovieDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalMovieDataSourceImplTest {

    private lateinit var movieDao: MovieDao
    private lateinit var dataSource: LocalMovieDataSourceImpl

    @BeforeEach
    fun setup() {
        movieDao = mockk(relaxed = true)
        dataSource = LocalMovieDataSourceImpl(movieDao)
    }

    @Test
    fun `addMovie maps dto and calls dao`() = runTest {
        val movie = movieDto1
        val entity = movie.toEntity()

        coEvery { movieDao.upsertMovie(entity) } just Runs

        dataSource.addMovie(movieDto1)

        coVerify { movieDao.upsertMovie(entity) }
    }

    @Test
    fun `getMovieById returns mapped dto`() = runTest {
        coEvery { movieDao.getMovieById(2L) } returns localMovieDto1

        val result = dataSource.getMovieById(2L)

        Assertions.assertEquals(localMovieDto1.toDto(), result)
        coVerify { movieDao.getMovieById(2L) }
    }

    @Test
    fun `getAllMovies returns mapped flow`() = runTest {
        val movies = listOf(
            localMovieDto1,
            localMovieDto2
        )
        val flowData = flowOf(movies)

        coEvery { movieDao.getAllMovies() } returns flowData

        val result = dataSource.getAllMovies().first()

        Assertions.assertEquals(movies.map { it.toDto() }, result)
    }

    @Test
    fun `deleteMovieById calls dao with correct id`() = runTest {
        val id = 10L
        coEvery { movieDao.deleteMovieById(id) } just Runs

        dataSource.deleteMovieById(id)

        coVerify { movieDao.deleteMovieById(id) }
    }

    @Test
    fun `deleteAllMovies clears dao`() = runTest {
        coEvery { movieDao.deleteAll() } just Runs

        dataSource.deleteAllMovies()

        coVerify { movieDao.deleteAll() }
    }

    @Test
    fun `updateMovie upserts mapped entity`() = runTest {
        val entity = movieDto2.toEntity()

        coEvery { movieDao.upsertMovie(entity) } just Runs

        dataSource.updateMovie(movieDto2)

        coVerify { movieDao.upsertMovie(entity) }
    }

    @Test
    fun `searchMoviesByTitle returns matched mapped list`() = runTest {
        val title = "Matrix"
        val entities = listOf(
            localMovieDto1,
            localMovieDto2
        )

        coEvery { movieDao.searchMoviesByTitle(title) } returns entities

        val result = dataSource.searchMoviesByTitle(title)

        Assertions.assertEquals(entities.map { it.toDto() }, result)
        coVerify { movieDao.searchMoviesByTitle(title) }
    }

    val localMovieDto1 = LocalMovieDto(
        id = 1L,
        title = "Neon Rewind",
        genres = listOf("Sci-Fi", "Thriller"),
        imdbRating = 8.3,
        userRating = 8.8,
        releaseDate = "2025-07-01",
        overview = "A time-traveling DJ must remix reality to fix a fractured timeline.",
        posterPictureURL = "https://example.com/posters/neon_rewind.jpg",
        runtimeMinutes = 127
    )

    val localMovieDto2 = LocalMovieDto(
        id = 2L,
        title = "Echoes of Mars",
        genres = listOf("Adventure", "Fantasy"),
        imdbRating = 7.6,
        userRating = null,
        releaseDate = "2024-11-15",
        overview = "When voices from beneath the Martian crust awaken, a group of colonists must make contact.",
        posterPictureURL = "https://example2.com/posters/neon_rewind.jpg",
        runtimeMinutes = 128
    )
    val movieDto1 = MovieDto(
        id = 1001L,
        title = "Neon Rewind",
        genres = listOf(
            GenreDto(id = 1L, name = "Sci-Fi", type = GenreDto.GenreType.MOVIE),
            GenreDto(id = 2L, name = "Thriller", type = GenreDto.GenreType.MOVIE)
        ),
        imdbRating = 8.4,
        userRating = 9.1,
        releaseDate = "2025-07-01",
        overview = "A time-traveling DJ rewinds reality through vinyl-powered wormholes.",
        posterPictureURL = "https://example.com/posters/neon_rewind.jpg",
        runtimeMinutes = 124
    )

    val movieDto2 = MovieDto(
        id = 1002L,
        title = "Echoes of Mars",
        genres = listOf(
            GenreDto(id = 3L, name = "Adventure", type = GenreDto.GenreType.MOVIE),
            GenreDto(id = 4L, name = "Fantasy", type = GenreDto.GenreType.MOVIE)
        ),
        imdbRating = 7.6,
        userRating = null,
        releaseDate = "2024-11-15",
        overview = "Martian whispers haunt the corridors of a crumbling colony.",
        posterPictureURL = "https://example.com/posters/echoes_of_mars.jpg",
        runtimeMinutes = 106
    )

}