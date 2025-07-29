package com.baghdad.local_datasource


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
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
class RecentSearchDaoTest {
    private lateinit var database: NovixDatabase
    private lateinit var recentSearchDao: RecentSearchDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        recentSearchDao = database.recentSearchDao()
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun addRecentSearch_insertsCorrectly() = runBlocking {
        // Given
        val item = RecentSearch(query = "Inception")

        // When
        recentSearchDao.addRecentSearch(item)
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Inception", result.first().query)
    }

    @Test
    fun deleteRecentSearchById_removesTargetItem() = runBlocking {
        // Given
        val item = RecentSearch(query = "To Delete")
        recentSearchDao.addRecentSearch(item)
        val id = recentSearchDao.getAllRecentSearch().first().find { it.query == "To Delete" }?.id
        requireNotNull(id) { "Inserted item not found in database" }

        // When
        recentSearchDao.deleteRecentSearchById(id)
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertTrue(result.none { it.id == id })
    }

    @Test
    fun clearAllRecentSearch_removesEverything() = runBlocking {
        // Given
        recentSearchDao.addRecentSearch(RecentSearch(query = "Clear 1"))
        recentSearchDao.addRecentSearch(RecentSearch(query = "Clear 2"))

        // When
        recentSearchDao.clearAllRecentSearch()
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertTrue(result.isEmpty())
    }
}