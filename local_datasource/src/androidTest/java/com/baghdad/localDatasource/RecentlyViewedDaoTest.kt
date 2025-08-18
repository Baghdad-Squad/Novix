package com.baghdad.localDatasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
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
    fun upsertRecentlyViewed_shouldInsertItem_whenCalledWithNewItem() = runBlocking {
        val recentViewedItem = movieRecentView

        recentlyViewedDao.upsertRecentlyViewed(recentViewedItem)

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertTrue(result.contains(movieRecentView))
    }

    @Test
    fun deleteAllRecentlyViewed_shouldRemoveAllItems_whenItCalled() = runBlocking {
        recentlyViewedDao.upsertRecentlyViewed(movieRecentView)
        recentlyViewedDao.upsertRecentlyViewed(tvShowRecentView)

        recentlyViewedDao.deleteAllRecentlyViewed()

        val result = recentlyViewedDao.getAllRecentlyViewed().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun getAllRecentlyViewed_shouldReturnAllInsertedItems_whenMultipleItemsExist() = runTest {
        val expectedItems = listOf(movieRecentView, tvShowRecentView)
        expectedItems.forEach { recentlyViewedDao.upsertRecentlyViewed(it) }

        val result = recentlyViewedDao.getAllRecentlyViewed().first()

        assertThat(result).containsExactlyElementsIn(expectedItems)
    }

    companion object {
        val movieRecentView = RecentlyViewed(
            contentId = 101L,
            contentType = "movie",
            contentImageURL = "https://example.com/images/movie_101.jpg",
            viewedAt = System.currentTimeMillis()
        )
        val tvShowRecentView = RecentlyViewed(
            contentId = 102L,
            contentType = "tv_show",
            contentImageURL = "https://example.com/images/tv_102.jpg",
            viewedAt = System.currentTimeMillis()
        )
    }
}
