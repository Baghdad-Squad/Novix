package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.SearchQueryDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

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
    fun tearDown() {
        database.close()
    }

    @Test
    fun addSearchQuery_insertsQuery() = runBlocking {
        // Given
        searchQueryDao.addSearchQuery(fakeSearchQueries1)

        // When
        val allQueries = searchQueryDao.getAllSearchQueries()

        // Then
        assertTrue(allQueries.contains(fakeSearchQueries1))
    }

    @Test
    fun addSearchQueries_insertsMultipleQueries() = runBlocking {
        // Given
        val queries = listOf(fakeSearchQueries1, fakeSearchQueries2)

        // When
        searchQueryDao.addSearchQueries(queries)

        // Then
        val result = searchQueryDao.getAllSearchQueries()
        assertEquals(2, result.size)
        assertTrue(result.containsAll(queries))
    }

    @Test
    fun getInvalidSearchQueries_returnsOnlyOldQueries() = runBlocking {
        // Given
        val threshold = System.currentTimeMillis()

        // When
        searchQueryDao.addSearchQueries(listOf(fakeSearchQueries1, fakeSearchQueries2))

        // Then
        val result = searchQueryDao.getInvalidSearchQueries(threshold)
        assertEquals(listOf(fakeSearchQueries1), result)
    }

    @Test
    fun getSearchByQuery_returnsCorrectMatch() = runBlocking {
        // When
        searchQueryDao.addSearchQuery(fakeSearchQueries1)

        // Then
        val result = searchQueryDao.getSearchByQuery("Avengers", "movie")
        assertEquals(listOf(fakeSearchQueries1), result)
    }

    @Test
    fun deleteSearchQueryByName_removesSingleEntry() = runBlocking {
        // When
        searchQueryDao.addSearchQuery(fakeSearchQueries1)
        searchQueryDao.deleteSearchQueryByName("IronMan")

        // Then
        val result = searchQueryDao.getAllSearchQueries()
        assertFalse(result.any { it.queryName == "IronMan" })
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

    @Test
    fun deleteInvalidSearchQueries_removesOldQueriesOnly() = runBlocking {
        // Given
        val threshold = System.currentTimeMillis()

        // When
        searchQueryDao.addSearchQueries(listOf(fakeSearchQueries1, fakeSearchQueries2))
        searchQueryDao.deleteInvalidSearchQueries(threshold)

        // Then
        val remaining = searchQueryDao.getAllSearchQueries()
        assertEquals(listOf(fakeSearchQueries2), remaining)
    }

    val fakeSearchQueries1 = SearchQuery(
        queryName = "space_adventures",
        mediaId = 101L,
        mediaType = "MOVIE",
        timeStamp = System.currentTimeMillis() - 60_000 // 1 min ago
    )
    val fakeSearchQueries2 = SearchQuery(
        queryName = "epic_fantasy",
        mediaId = 102L,
        mediaType = "MOVIE",
        timeStamp = System.currentTimeMillis() - 120_000 // 2 mins ago
    )
}