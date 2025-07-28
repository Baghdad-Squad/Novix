package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.ContinueWatching
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ContinueWatchingDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var continueWatchingDao: ContinueWatchingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        continueWatchingDao = database.continueWatchingDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun upsertContinueWatching_insertsItemCorrectly() = runBlocking {
        val item = ContinueWatching(
            contentId = 1L,
            userId = 100L,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "image_url",
            contentType = "movie",
        )

        continueWatchingDao.upsertContinueWatching(item)

        val result =
            continueWatchingDao.getContinueWatching(userId = 100L, pageSize = 1, offset = 0)

        assertEquals(item, result.first())
    }

    @Test
    fun getContinueWatching_returnsPaginatedResults() = runBlocking {
        val userId = 100L
        val items = List(15) { index ->
            ContinueWatching(
                contentId = index.toLong(),
                userId = userId,
                genreIds = listOf(1L, 2L),
                contentImageUrl = "image_url_$index",
                contentType = "movie",
            )
        }

        items.forEach { continueWatchingDao.upsertContinueWatching(it) }

        val page = continueWatchingDao.getContinueWatching(
            userId = userId,
            pageSize = 5,
            offset = 5
        )

        assertEquals(5, page.size)
//        assertTrue(page.all { it.userId == userId })
    }

    @Test
    fun kgetContinueWatching_returnsPaginatedResults() = runBlocking {
        val userId = 100L
        val items = List(15) { index ->
            ContinueWatching(
                contentId = index.toLong(),  // Unique IDs
                userId = userId,
                genreIds = listOf(1L, 2L),
                contentImageUrl = "image_url_$index",
                contentType = "movie"
            )
        }

        items.forEach { continueWatchingDao.upsertContinueWatching(it) }

        // Fetch page 2 (items 5-9)
        val page = continueWatchingDao.getContinueWatching(
            userId = userId,
            pageSize = 5,
            offset = 5
        )

        // Verify the returned items have IDs 5..9
        assertEquals(5, page.size)
        assertEquals(listOf(5L, 6L, 7L, 8L, 9L), page.map { it.contentId })
    }

}