package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.database.dao.RecentSearchDao
import com.baghdad.local_datasource.database.dto.LocalRecentSearchDto
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
        val item = LocalRecentSearchDto(query = "Inception")
        recentSearchDao.addRecentSearch(item)

        val result = recentSearchDao.getAllRecentSearch().first()
        assertEquals(1, result.size)
        assertEquals("Inception", result.first().query)
    }

    @Test
    fun deleteRecentSearchById_removesTargetItem() = runBlocking {
        val item = LocalRecentSearchDto(query = "To Delete")
        recentSearchDao.addRecentSearch(item)
        val id = recentSearchDao.getAllRecentSearch().first().first().id

        recentSearchDao.deleteRecentSearchById(id)
        val result = recentSearchDao.getAllRecentSearch().first()
        assertTrue(result.none { it.id == id })
    }

    @Test
    fun clearAllRecentSearch_removesEverything() = runBlocking {
        recentSearchDao.addRecentSearch(LocalRecentSearchDto(query = "Clear 1"))
        recentSearchDao.addRecentSearch(LocalRecentSearchDto(query = "Clear 2"))

        recentSearchDao.clearAllRecentSearch()
        val result = recentSearchDao.getAllRecentSearch().first()
        assertTrue(result.isEmpty())
    }

}