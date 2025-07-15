package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.database.dao.GenreDao
import com.baghdad.local_datasource.database.dto.LocalGenreDto
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
        val genre = LocalGenreDto(
            id = 1L,
            name = "name1",
            type = "type1"
        )
        genreDao.addGenre(genre)

        val result = genreDao.getGenreById(1L)
        assertEquals(genre.name, result.name)
        assertEquals(genre.id, result.id)
    }

    @Test
    fun getAllGenres_returnsAllItems() {
        val genres = listOf(
            LocalGenreDto(id = 1L, name = "name2", type = "type2"),
            LocalGenreDto(id = 2L, name = "name3", type = "type3")
        )
        genres.forEach { genreDao.addGenre(it) }

        val result = genreDao.getAllGenres()
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "name2" })
        assertTrue(result.any { it.name == "name3" })
    }

    @Test
    fun deleteGenreById_removesSpecificItem() {
        val genre = LocalGenreDto(id = 3L, name = "name4", type = "type4")
        genreDao.addGenre(genre)

        genreDao.deleteGenreById(3L)
        val result = genreDao.getAllGenres()
        assertTrue(result.none { it.id == 3L })
    }

    @Test
    fun deleteAllGenres_removesEverything() {
        genreDao.addGenre(LocalGenreDto(4L, "name5", "type5"))
        genreDao.addGenre(LocalGenreDto(5L, "name6", "type6"))

        genreDao.deleteAllGenres()
        val result = genreDao.getAllGenres()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getGenreById_returnsCorrectGenre() {
        val genre = LocalGenreDto(id = 6L, name = "name7", "type7")
        genreDao.addGenre(genre)

        val result = genreDao.getGenreById(6L)
        assertEquals("name7", result.name)
    }
}
