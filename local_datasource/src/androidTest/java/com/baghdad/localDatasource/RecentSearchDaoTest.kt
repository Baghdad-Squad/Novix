package com.baghdad.localDatasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
import com.baghdad.localDatasource.roomDB.entity.RecentSearch
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
    fun upsertRecentSearch_shouldInsertOneItem_whenCalledWithNewQuery() = runBlocking {
        val item = RecentSearch(query = "Inception")

        recentSearchDao.upsertRecentSearch(item)
        val result = recentSearchDao.getAllRecentSearch().first()

        assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun upsertRecentSearch_shouldInsertCorrectQuery_whenCalledWithNewQuery() = runBlocking {
        val item = RecentSearch(query = "Fast and Furious")

        recentSearchDao.upsertRecentSearch(item)
        val result = recentSearchDao.getAllRecentSearch().first()

        assertThat(result.first().query).isEqualTo("Fast and Furious")
    }

    @Test
    fun deleteRecentSearchById_shouldDeleteOnlyTargetItem_whenItemExists() = runBlocking {
        val itemToDelete = RecentSearch(query = "Fast and Furious")
        recentSearchDao.upsertRecentSearch(itemToDelete)
        val insertedItem = recentSearchDao.getAllRecentSearch().first().single()

        recentSearchDao.deleteRecentSearchById(insertedItem.id)

        val remainingItems = recentSearchDao.getAllRecentSearch().first()
        assertThat(remainingItems).doesNotContain(insertedItem)
    }

    @Test
    fun clearAllRecentSearch_shouldRemoveAllItems_whenDatabaseHasEntries() = runBlocking {
        recentSearchDao.upsertRecentSearch(RecentSearch(query = "Fast and Furious"))
        recentSearchDao.upsertRecentSearch(RecentSearch(query = "Spongebob Square Pants"))

        recentSearchDao.clearAllRecentSearch()

        val result = recentSearchDao.getAllRecentSearch().first()
        assertThat(result).isEmpty()
    }

}
