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
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

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
        database.close()
    }

    @Test
    fun addFavoriteGenre_insertsGenre() = runBlocking {
        val genre = LocalFavoriteGenreDto(
            genreId = 1L,
            name = "Action",
            count = 1,
            timeStamp = System.currentTimeMillis()
        )

        favoriteGenreDao.addFavoriteGenre(genre)

        val result = favoriteGenreDao.getFavoriteGenreById(1L)
        assertNotNull(result)
    }

    @Test
    fun getFavoriteGenreById_returnsCorrectGenre() = runBlocking {
        val genre = LocalFavoriteGenreDto(
            genreId = 2L,
            name = "Drama",
            count = 2,
            timeStamp = System.currentTimeMillis()
        )

        favoriteGenreDao.addFavoriteGenre(genre)

        val result = favoriteGenreDao.getFavoriteGenreById(2L)
        assertEquals("Drama", result?.name)
        assertEquals(2, result?.count)
    }

    @Test
    fun getFavoriteGenres_ordersByCountAndTimeStamp() = runBlocking {
        val genres = listOf(
            LocalFavoriteGenreDto(1L, "Comedy", 3, System.currentTimeMillis() - 10000),
            LocalFavoriteGenreDto(2L, "Drama", 3, System.currentTimeMillis() - 5000),
            LocalFavoriteGenreDto(3L, "Sci-Fi", 2, System.currentTimeMillis())
        )

        genres.forEach { favoriteGenreDao.addFavoriteGenre(it) }

        val result = favoriteGenreDao.getFavoriteGenres()
        assertEquals(3, result.size)
        assertEquals("Drama", result[0].name)
        assertEquals("Comedy", result[1].name)
        assertEquals("Sci-Fi", result[2].name)
    }

    @Test
    fun updateFavoriteGenreCount_insertsNewIfNotExists() = runBlocking {
        val genreId = 10L
        val name = "Thriller"

        favoriteGenreDao.updateFavoriteGenreCount(genreId, name)
        val genre = favoriteGenreDao.getFavoriteGenreById(genreId)

        assertEquals(1, genre?.count)
        assertEquals("Thriller", genre?.name)
    }

    @Test
    fun updateFavoriteGenreCount_incrementsCountIfExists() = runBlocking {
        val genre = LocalFavoriteGenreDto(
            genreId = 11L,
            name = "Horror",
            count = 2,
            timeStamp = System.currentTimeMillis()
        )

        favoriteGenreDao.addFavoriteGenre(genre)
        favoriteGenreDao.updateFavoriteGenreCount(11L, "Horror")

        val result = favoriteGenreDao.getFavoriteGenreById(11L)
        assertEquals(3, result?.count)
    }
}