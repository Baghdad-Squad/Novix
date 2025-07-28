package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class RecentlyViewedDaoTest {
    private lateinit var database: NovixDatabase
    private lateinit var recentlyViewedDao: RecentlyViewedDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        recentlyViewedDao = database.recentViewedDao()
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun upsertRecentlyViewed_insertsItem() = runBlocking {

        recentlyViewedDao.upsertRecentlyViewed(fakeRecentViews1)

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertTrue(result.contains(fakeRecentViews1))
    }

    @Test
    fun upsertRecentlyViewed_replacesExistingItem() = runBlocking {

        recentlyViewedDao.upsertRecentlyViewed(fakeRecentViews1)
        recentlyViewedDao.upsertRecentlyViewed(fakeRecentViews2)

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertEquals(1, result.size)
        assertEquals(2222L, result.first())
    }

    @Test
    fun clearAllRecentlyViewed_removesAllItems() = runBlocking {
        val items = listOf(fakeRecentViews1, fakeRecentViews2)


        items.forEach { recentlyViewedDao.upsertRecentlyViewed(it) }
        recentlyViewedDao.clearAllRecentlyViewed()

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getAllRecentlyViewed_returnsAllInsertedItems() = runBlocking {
        val items = listOf(fakeRecentViews1, fakeRecentViews2)


        items.forEach { recentlyViewedDao.upsertRecentlyViewed(it) }

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertEquals(3, result.size)
        assertTrue(result.containsAll(items))
    }

    val fakeRecentViews1 = RecentlyViewed(
        contentId = 101L,
        contentType = "movie",
        contentImageURL = "https://example.com/images/movie_101.jpg",
        viewedAt = System.currentTimeMillis() - 60_000
    )
    val fakeRecentViews2 = RecentlyViewed(
        contentId = 102L,
        contentType = "tv_show",
        contentImageURL = "https://example.com/images/tv_102.jpg",
        viewedAt = System.currentTimeMillis() - 3_600_000
    )
}