package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.google.common.truth.Truth.assertThat
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
    fun shouldInsertMovieCorrectly_whenUpsertMovieIsCalled() = runBlocking {
        // Given
        movieDao.upsertMovie(movie1)

        // When
        val result = movieDao.getMovieById(1L)

        // Then
        assertThat(result.id).isEqualTo(movie1.id)
        assertThat(result.title).isEqualTo(movie1.title)
        assertThat(result.genres).isEqualTo(movie1.genres)
    }

    @Test
    fun shouldReturnCorrectMovie_whenGetMovieByIdIsCalled() = runBlocking {
        // Given
        movieDao.upsertMovie(movie1)

        // When
        val result = movieDao.getMovieById(1L)

        // Then
        assertThat(result.title).isEqualTo("Shadow Circuit")
    }

    @Test
    fun shouldRemoveSpecificMovie_whenDeleteMovieByIdIsCalled() = runBlocking {
        // Given
        movieDao.upsertMovie(movie1)

        // When
        movieDao.deleteMovieById(1L)
        val result = movieDao.getAllMovies().first()

        // Then
        assertThat(result.none { it.id == 1L }).isTrue()
    }

    @Test
    fun shouldRemoveAllMovies_whenDeleteAllIsCalled() = runBlocking {
        // Given
        movieDao.upsertMovie(movie1)
        movieDao.upsertMovie(movie2)

        // When
        movieDao.deleteAll()
        val result = movieDao.getAllMovies().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun shouldReturnAllMovies_whenGetAllMoviesIsCalled() = runBlocking {
        // Given
        movieDao.upsertMovie(movie1)
        movieDao.upsertMovie(movie2)

        // When
        val result = movieDao.getAllMovies().first()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.any { it.title.contains("Shadow Circuit") }).isTrue()
        assertThat(result.any { it.title.contains("Celestial Drift") }).isTrue()
    }

    val movie1 = Movie(
        id = 1L,
        title = "Shadow Circuit",
        genres = listOf(1L, 2L, 3L),
        imdbRating = 8.1,
        userRating = 9.0,
        releaseDate = "2024-06-12",
        overview = "A rogue hacker battles corrupt corporations in a neon-drenched future.",
        posterPictureURL = "https://example.com/shadow.jpg",
        runtimeMinutes = 132
    )

    val movie2 = Movie(
        id = 2L,
        title = "Celestial Drift",
        genres = listOf(4L, 5L, 6L),
        imdbRating = 7.5,
        userRating = null,
        releaseDate = "2023-11-01",
        overview = "Two strangers discover they’re sharing the same dreams, but only one can remember.",
        posterPictureURL = "https://example.com/celestial.jpg",
        runtimeMinutes = 118
    )
}
