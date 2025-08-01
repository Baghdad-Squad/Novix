package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    fun shouldInsertItem_whenUpsertRecentlyViewedIsCalled() = runBlocking {
        // Given
        recentlyViewedDao.upsertRecentlyViewed(recentView1)

        // When
        val result = recentlyViewedDao.getAllRecentlyViewed().first()

        // Then
        assertTrue(result.contains(recentView1))
    }

    @Test
    fun shouldReplaceExistingItem_whenUpsertRecentlyViewedIsCalledTwice() = runBlocking {
        // Given
        recentlyViewedDao.upsertRecentlyViewed(recentView1)
        recentlyViewedDao.upsertRecentlyViewed(recentView2)

        // When
        val result = recentlyViewedDao.getAllRecentlyViewed().first()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.first()).isEqualTo(recentView1)
    }

    @Test
    fun shouldRemoveAllItems_whenClearAllRecentlyViewedIsCalled() = runBlocking {
        // Given
        val items = listOf(recentView1, recentView2)
        items.forEach { recentlyViewedDao.upsertRecentlyViewed(it) }
        recentlyViewedDao.clearAllRecentlyViewed()

        // When
        val result = recentlyViewedDao.getAllRecentlyViewed().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun shouldReturnAllInsertedItems_whenGetAllRecentlyViewedIsCalled() = runBlocking {
        // Given
        val items = listOf(recentView1, recentView2)
        items.forEach { recentlyViewedDao.upsertRecentlyViewed(it) }

        // When
        val result = recentlyViewedDao.getAllRecentlyViewed().first()

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.containsAll(items)).isTrue()
    }

    companion object {
        val recentView1 = RecentlyViewed(
            contentId = 101L,
            contentType = "movie",
            contentImageURL = "https://example.com/images/movie_101.jpg",
            viewedAt = System.currentTimeMillis() - 60_000
        )
        val recentView2 = RecentlyViewed(
            contentId = 102L,
            contentType = "tv_show",
            contentImageURL = "https://example.com/images/tv_102.jpg",
            viewedAt = System.currentTimeMillis() - 3_600_000
        )
    }
}
