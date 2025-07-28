package com.baghdad.local_datasource


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.Genre
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class GenreDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var genreDao: GenreDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        genreDao = database.genreDao()
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun addGenre_insertsCorrectly() {
        // Given
        val genre = Genre(
            id = 1L,
            name = "name1",
            type = "type1"
        )

        // When
        genreDao.addGenre(genre)

        // Then
        val result = genreDao.getGenreById(1L)
        assertEquals(genre.name, result.name)
        assertEquals(genre.id, result.id)
    }

    @Test
    fun getGenresByIds_returnsCorrectGenres() {
        // Given
        val genre = Genre(
            id = 2L,
            name = "name2",
            type = "type2"
        )
        // When
        genreDao.addGenre(genre)

        // Then
        val result = genreDao.getGenreById(2L)
        assertEquals(genre, result)
    }

    @Test
    fun getAllGenres_returnsAllItems() {
        // Given
        val genres = listOf(
            Genre(id = 1L, name = "name2", type = "type2"),
            Genre(id = 2L, name = "name3", type = "type3")
        )

        // When
        genres.forEach { genreDao.addGenre(it) }

        // Then
        val result = genreDao.getAllGenres()
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "name2" })
        assertTrue(result.any { it.name == "name3" })
    }

    @Test
    fun deleteGenreById_removesSpecificItem() {
        // Given
        val genre = Genre(id = 3L, name = "name4", type = "type4")

        // When
        genreDao.addGenre(genre)
        genreDao.deleteGenreById(3L)

        // Then
        val result = genreDao.getAllGenres()
        assertTrue(result.none { it.id == 3L })
    }

    @Test
    fun deleteAllGenres_removesEverything() {
        // Given
        genreDao.addGenre(Genre(4L, "name5", "type5"))
        genreDao.addGenre(Genre(5L, "name6", "type6"))

        // When
        genreDao.deleteAllGenres()

        // Then
        val result = genreDao.getAllGenres()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getGenreById_returnsCorrectGenre() {
        // Given
        val genre = Genre(id = 6L, name = "name7", "type7")

        // When
        genreDao.addGenre(genre)

        // Then
        val result = genreDao.getGenreById(6L)
        assertEquals("name7", result.name)
    }
}