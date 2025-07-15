package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.database.dao.TvShowDao
import com.baghdad.local_datasource.database.dto.LocalTvShowDto
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
class TvShowDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var dao: TvShowDao

    private val fakeTvShow1 = LocalTvShowDto(
        id = 100L,
        title = "Quantum Echo",
        genres = listOf("Sci-Fi", "Drama"),
        imdbRating = 8.6,
        userRating = 9.2,
        releaseDate = "2025-01-15",
        overview = "Echoes of quantum futures disrupt timelines.",
        posterPictureURL = "https://example.com/quantum.jpg",
        numberOfSeasons = 2
    )

    private val fakeTvShow2 = LocalTvShowDto(
        id = 101L,
        title = "Shadow Empire",
        genres = listOf("Fantasy", "Adventure"),
        imdbRating = 7.9,
        userRating = 8.5,
        releaseDate = "2023-11-22",
        overview = "A mythical war for the lost throne of Arak.",
        posterPictureURL = "https://example.com/shadow.jpg",
        numberOfSeasons = 4
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.tvShowDao()
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun upsertTvShow_savesDataCorrectly() = runBlocking {
        dao.upsertTvShow(fakeTvShow1)

        val result = dao.getAllTvShow().first()

        assertEquals(1, result.size)
        assertEquals("Quantum Echo", result.first().title)
    }

    @Test
    fun getAllTvShow_returnsAllInsertedItems() = runBlocking {
        dao.upsertTvShow(fakeTvShow1)
        dao.upsertTvShow(fakeTvShow2)

        val result = dao.getAllTvShow().first()

        assertEquals(2, result.size)
    }

    @Test
    fun deleteTvShowById_removesTargetShow() = runBlocking {
        dao.upsertTvShow(fakeTvShow1)
        dao.deleteTvShowByID(fakeTvShow1.id)

        val result = dao.getAllTvShow().first()

        assertTrue(result.none { it.id == fakeTvShow1.id })
    }

    @Test
    fun deleteAll_removesAllShows() = runBlocking {
        dao.upsertTvShow(fakeTvShow1)
        dao.upsertTvShow(fakeTvShow2)

        dao.deleteAll()
        val result = dao.getAllTvShow().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun searchTvShowsByTitle_returnsMatchingResults() = runBlocking {
        dao.upsertTvShow(fakeTvShow1)
        dao.upsertTvShow(fakeTvShow2)

        val result = dao.searchTvShowsByTitle("Shadow")

        assertEquals(1, result.size)
        assertEquals("Shadow Empire", result.first().title)
    }

    @Test
    fun getTvShowById_returnsCorrectData() = runBlocking {
        dao.upsertTvShow(fakeTvShow2)

        val result = dao.getTvShowById(fakeTvShow2.id)

        assertEquals(fakeTvShow2.title, result.title)
        assertEquals(fakeTvShow2.numberOfSeasons, result.numberOfSeasons)
    }


}