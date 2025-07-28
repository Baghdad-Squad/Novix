package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.LocalFavoriteGenreDto
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteGenreDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var favoriteGenreDao: FavoriteGenreDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        favoriteGenreDao = database.favoriteGenreDao()
    }

    @After
    fun closeDataBase() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun addFavoriteGenre_insertsGenre() = runBlocking {
        // Given
        val genre = LocalFavoriteGenreDto(
            genreId = 1L,
            name = "Action",
            count = 1,
            timeStamp = System.currentTimeMillis()
        )
        favoriteGenreDao.addFavoriteGenre(genre)

        // When
        val result = favoriteGenreDao.getFavoriteGenreById(1L)

        // Then
        assertNotNull(result)
        assertEquals(genre.name, result?.name)
        assertEquals(genre.count, result?.count)
        assertEquals(genre.name, result?.name)
    }

    @Test
    fun getFavoriteGenreById_returnsCorrectGenre() = runBlocking {
        // Given
        val genre = LocalFavoriteGenreDto(
            genreId = 2L,
            name = "Drama",
            count = 2,
            timeStamp = System.currentTimeMillis()
        )
        favoriteGenreDao.addFavoriteGenre(genre)

        // When
        val result = favoriteGenreDao.getFavoriteGenreById(2L)

        // Then
        assertEquals("Drama", result?.name)
        assertEquals(2, result?.count)
    }

    @Test
    fun getFavoriteGenres_ordersByCountAndTimeStamp() = runBlocking {
        // Given
        val genres = listOf(
            LocalFavoriteGenreDto(1L, "Comedy", 3, 1L),
            LocalFavoriteGenreDto(2L, "Drama", 3, 2L),
            LocalFavoriteGenreDto(3L, "Sci-Fi", 2, 3L)
        )
        genres.forEach { favoriteGenreDao.addFavoriteGenre(it) }

        // When
        val result = favoriteGenreDao.getFavoriteGenres()

        // Then
        assertEquals(3, result.size)
        assertEquals("Sci-Fi", result[0].name)
        assertEquals("Drama", result[1].name)
        assertEquals("Comedy", result[2].name)
    }

    @Test
    fun updateFavoriteGenreCount_insertsNewIfNotExists() = runBlocking {
        // Given
        val genreId = 10L
        val name = "Thriller"
        favoriteGenreDao.updateFavoriteGenreCount(genreId, name)

        // When
        val result = favoriteGenreDao.getFavoriteGenreById(genreId)

        // Then
        assertEquals(1, result?.count)
        assertEquals("Thriller", result?.name)
    }

    @Test
    fun updateFavoriteGenreCount_incrementsCountIfExists() = runBlocking {
        // Given
        val genre = LocalFavoriteGenreDto(
            genreId = 11L,
            name = "Horror",
            count = 2,
            timeStamp = System.currentTimeMillis()
        )
        favoriteGenreDao.addFavoriteGenre(genre)
        favoriteGenreDao.updateFavoriteGenreCount(11L, "Horror")

        // When
        val result = favoriteGenreDao.getFavoriteGenreById(11L)

        // Then
        assertEquals(3, result?.count)
    }
}