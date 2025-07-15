package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.database.dao.MovieDao
import com.baghdad.local_datasource.database.dto.LocalMovieDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {
    private lateinit var dataBase: NovixDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setupDataBase() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        movieDao = dataBase.movieDao()
    }

    @After
    fun closeDataBase() {
        dataBase.close()
    }

    @Test
    fun upsertMovie_insertsCorrectly() = runBlocking {
        movieDao.upsertMovie(movie1)

        val result = movieDao.getMovieById(1L)
        assertEquals(movie1.title, result.title)
        assertEquals(movie1.genres, result.genres)
        assertEquals(movie1.id, result.id)
    }

    @Test
    fun getMovieById_returnsCorrectMovie() = runBlocking {
        movieDao.upsertMovie(movie1)

        val result = movieDao.getMovieById(1L)
        assertEquals("Shadow Circuit", result.title)
    }

    @Test
    fun deleteMovieById_removesSpecificMovie() = runBlocking {
        movieDao.upsertMovie(movie1)

        movieDao.deleteMovieById(1L)
        val allMovies = movieDao.getAllMovies().first()
        assertTrue(allMovies.none { it.id == 1L })
    }

    @Test
    fun deleteAll_removesEverything() = runBlocking {
        movieDao.upsertMovie(movie1)
        movieDao.upsertMovie(movie2)

        movieDao.deleteAll()
        val result = movieDao.getAllMovies().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getAllMovies_returnsAllInserted() = runBlocking {
        movieDao.upsertMovie(movie1)
        movieDao.upsertMovie(movie2)

        val result = movieDao.getAllMovies().first()
        assertEquals(2, result.size)
        assertTrue(result.any { it.title.contains("Shadow Circuit") })
        assertTrue(result.any { it.title.contains("Celestial Drift") })
    }

    @Test
    fun searchMoviesByTitle_returnsMatchingResults() = runBlocking {

        movieDao.upsertMovie(movie1)
        movieDao.upsertMovie(movie2)

        val result = movieDao.searchMoviesByTitle("Sha")
        assertEquals(1, result.size)
        assertTrue(result.all { it.title.contains("Sha") })
    }

    val movie1 = LocalMovieDto(
        id = 1L,
        title = "Shadow Circuit",
        genres = listOf("Action", "Sci-Fi"),
        imdbRating = 8.1,
        userRating = 9.0,
        releaseDate = "2024-06-12",
        overview = "A rogue hacker battles corrupt corporations in a neon-drenched future.",
        posterPictureURL = "https://example.com/shadow.jpg",
        runtimeMinutes = 132
    )

    val movie2 = LocalMovieDto(
        id = 2L,
        title = "Celestial Drift",
        genres = listOf("Drama", "Fantasy"),
        imdbRating = 7.5,
        userRating = null,
        releaseDate = "2023-11-01",
        overview = "Two strangers discover they’re sharing the same dreams, but only one can remember.",
        posterPictureURL = "https://example.com/celestial.jpg",
        runtimeMinutes = 118
    )


}