package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.google.common.truth.Truth.assertThat
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
    fun shouldInsertRecentSearchCorrectly_whenUpsertRecentSearchIsCalled() = runBlocking {
        // Given
        val item = RecentSearch(query = "Inception")

        // When
        recentSearchDao.upsertRecentSearch(item)
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertThat(result.size).isEqualTo(1)
        assertThat(result.first().query).isEqualTo("Inception")
    }

    @Test
    fun shouldRemoveTargetItem_whenDeleteRecentSearchByIdIsCalled() = runBlocking {
        // Given
        val item = RecentSearch(query = "To Delete")
        recentSearchDao.upsertRecentSearch(item)
        val id = recentSearchDao.getAllRecentSearch().first().find { it.query == "To Delete" }?.id
        requireNotNull(id) { "Inserted item not found in database" }

        // When
        recentSearchDao.deleteRecentSearchById(id)
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertThat(result.none { it.id == id }).isTrue()
    }

    @Test
    fun shouldRemoveAllItems_whenClearAllRecentSearchIsCalled() = runBlocking {
        // Given
        recentSearchDao.upsertRecentSearch(RecentSearch(query = "Clear 1"))
        recentSearchDao.upsertRecentSearch(RecentSearch(query = "Clear 2"))

        // When
        recentSearchDao.clearAllRecentSearch()
        val result = recentSearchDao.getAllRecentSearch().first()

        // Then
        assertThat(result).isEmpty()
    }
}
