//package com.baghdad.local_datasource
//
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.SmallTest
//import com.baghdad.local_datasource.roomDB.database.NovixDatabase
//import com.baghdad.local_datasource.roomDB.entity.SearchQuery
//import com.google.common.truth.Truth.assertThat
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class SearchQueryDaoTest {
//    private lateinit var database: NovixDatabase
//    private lateinit var searchQueryDao: SearchQueryDao
//
//    @Before
//    fun setup() {
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            NovixDatabase::class.java
//        ).allowMainThreadQueries().build()
//
//        searchQueryDao = database.searchQueryDao()
//    }
//
//    @After
//    fun closeDatabase() {
//        database.close()
//    }
//
//    @Test
//    fun shouldInsertSingleQuery_whenAddSearchQueryIsCalled() = runBlocking {
//        // When
//        searchQueryDao.addSearchQuery(SEARCH_QUERY)
//        val result = searchQueryDao.getAllSearchQueries()
//
//        // Then
//        assertThat(result.size).isEqualTo(1)
//        assertThat(result.map { it.queryName }.containsAll(result.map { it.queryName })).isTrue()
//    }
//
//    @Test
//    fun shouldInsertMultipleQueries_whenAddSearchQueriesIsCalled() = runBlocking {
//        // When
//        searchQueryDao.addSearchQueries(QUERY)
//        val result = searchQueryDao.getAllSearchQueries()
//
//        // Then
//        assertThat(result.size).isEqualTo(2)
//        assertThat(result.map { it.queryName }.containsAll(QUERY.map { it.queryName })).isTrue()
//    }
//
//    @Test
//    fun shouldReturnOldQueriesOnly_whenGetInvalidSearchQueriesIsCalled() = runBlocking {
//        // Given
//        val threshold = 120_000L
//
//        // When
//        searchQueryDao.addSearchQueries(QUERY)
//
//        // Then
//        val result = searchQueryDao.getInvalidSearchQueries(threshold)
//        assertThat(result.map { it.queryName }).isEqualTo(listOf(SEARCH_QUERY.queryName))
//    }
//
//    @Test
//    fun shouldReturnCorrectQuery_whenGetSearchByQueryIsCalled() = runBlocking {
//        // When
//        searchQueryDao.addSearchQuery(SEARCH_QUERY)
//
//        // Then
//        val result = searchQueryDao.getSearchByQuery("space_adventures", "MOVIE")
//        assertThat(result.first().queryName).isEqualTo("space_adventures")
//    }
//
//    @Test
//    fun shouldRemoveSingleEntry_whenDeleteSearchQueryByNameIsCalled() = runBlocking {
//        // When
//        searchQueryDao.addSearchQuery(SEARCH_QUERY)
//        searchQueryDao.deleteSearchQueryByName("space_adventures")
//
//        // Then
//        val result = searchQueryDao.getAllSearchQueries()
//        assertThat(result.any { it.queryName == "space_adventures" }).isFalse()
//    }
//
//    @Test
//    fun shouldClearDatabase_whenDeleteAllSearchQueriesIsCalled() = runBlocking {
//        // When
//        searchQueryDao.addSearchQueries(QUERY)
//        searchQueryDao.deleteAllSearchQueries()
//
//        // Then
//        val result = searchQueryDao.getAllSearchQueries()
//        assertThat(result).isEmpty()
//    }
//
//    companion object {
//        val SEARCH_QUERY = SearchQuery(
//            queryName = "space_adventures",
//            mediaId = 101L,
//            mediaType = "MOVIE",
//            timeStamp = 60_000
//        )
//        val QUERY = listOf(
//            SEARCH_QUERY,
//            SEARCH_QUERY.copy(queryName = "epic_fantasy", timeStamp = 120_000)
//        )
//    }
//}
