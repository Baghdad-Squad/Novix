package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.SearchQueryDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@SmallTest

class SearchQueryDaoTest {
    private lateinit var database: NovixDatabase
    private lateinit var searchQueryDao: SearchQueryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        searchQueryDao = database.searchQueryDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun addSearchQuery_insertsQuery() = runBlocking {
        // Given
        searchQueryDao.addSearchQuery(fakeSearchQueries1)

        // When
        val result = searchQueryDao.getAllSearchQueries()

        // Then
        assertEquals(1, result.size)
        assertTrue(result.map { it.queryName }.containsAll(result.map { it.queryName }))
    }

    @Test
    fun addSearchQueries_insertsMultipleQueries() = runBlocking {
        // Given
        val queries = listOf(fakeSearchQueries1, fakeSearchQueries2)

        // When
        searchQueryDao.addSearchQueries(queries)
        val result = searchQueryDao.getAllSearchQueries()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.map { it.queryName }.containsAll(queries.map { it.queryName }))
    }

    @Test
    fun getInvalidSearchQueries_returnsOnlyOldQueries() = runBlocking {
        // Given
        val threshold = 120_000L

        // When
        searchQueryDao.addSearchQueries(listOf(fakeSearchQueries1, fakeSearchQueries2))

        // Then
        val result = searchQueryDao.getInvalidSearchQueries(threshold)
        assertEquals(listOf(fakeSearchQueries1.queryName), result.map { it.queryName })
    }

    @Test
    fun getSearchByQuery_returnsCorrectMatch() = runBlocking {
        // When
        searchQueryDao.addSearchQuery(fakeSearchQueries1)

        // Then
        val result = searchQueryDao.getSearchByQuery("space_adventures", "MOVIE")
        assertEquals("space_adventures", result.first().queryName)
    }

    @Test
    fun deleteSearchQueryByName_removesSingleEntry() = runBlocking {
        // When
        searchQueryDao.addSearchQuery(fakeSearchQueries1)
        searchQueryDao.deleteSearchQueryByName("space_adventures")

        // Then
        val result = searchQueryDao.getAllSearchQueries()
        assertFalse(result.any { it.queryName == "space_adventures" })
    }

    @Test
    fun deleteAllSearchQueries_clearsDatabase() = runBlocking {
        // Given
        val queries = listOf(fakeSearchQueries1, fakeSearchQueries2)

        // When
        searchQueryDao.addSearchQueries(queries)
        searchQueryDao.deleteAllSearchQueries()

        // Then
        val result = searchQueryDao.getAllSearchQueries()
        assertTrue(result.isEmpty())
    }

    val fakeSearchQueries1 = SearchQuery(
        queryName = "space_adventures",
        mediaId = 101L,
        mediaType = "MOVIE",
        timeStamp = 60_000
    )
    val fakeSearchQueries2 = SearchQuery(
        queryName = "epic_fantasy",
        mediaId = 102L,
        mediaType = "MOVIE",
        timeStamp = 120_000
    )
}