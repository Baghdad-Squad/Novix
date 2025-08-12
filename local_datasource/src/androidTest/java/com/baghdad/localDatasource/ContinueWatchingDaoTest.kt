package com.baghdad.localDatasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.localDatasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
import com.baghdad.localDatasource.roomDB.entity.ContinueWatching
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ContinueWatchingDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var continueWatchingDao: ContinueWatchingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()
        continueWatchingDao = database.continueWatchingDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun shouldInsertItemCorrectly_whenUpsertContinueWatchingIsCalled() = runBlocking {
        // When
        continueWatchingDao.upsertContinueWatching(FAKE_CONTINUE_WATCHING_1)
        val result = continueWatchingDao.getContinueWatching(TEST_USER_ID, 1, 0)

        // Then
        assertThat(result.first()).isEqualTo(FAKE_CONTINUE_WATCHING_1)
    }

    @Test
    fun shouldUpdateItem_whenUpsertContinueWatchingCalledWithSameContentId() = runBlocking {
        // Given
        continueWatchingDao.upsertContinueWatching(FAKE_CONTINUE_WATCHING_1)

        // When
        val updated = FAKE_CONTINUE_WATCHING_1.copy(contentImageUrl = "new_url")
        continueWatchingDao.upsertContinueWatching(updated)
        val result = continueWatchingDao.getContinueWatching(TEST_USER_ID, 1, 0)

        // Then
        assertThat(result.first().contentImageUrl).isEqualTo("new_url")
    }

    @Test
    fun shouldReturnPaginatedResultsInDescendingOrder_whenGetContinueWatchingIsCalled() =
        runBlocking {
            // Given
            FAKE_ITEMS.forEach { continueWatchingDao.upsertContinueWatching(it) }

            // When
            val page =
                continueWatchingDao.getContinueWatching(TEST_USER_ID, pageSize = 5, offset = 5)

            // Then
            assertThat(page.size).isEqualTo(5)
            assertThat(page.map { it.contentId }).isEqualTo(listOf(9L, 8L, 7L, 6L, 5L))
        }

    @Test
    fun shouldObserveContinueWatching_whenNewItemIsInserted() = runBlocking {
        // When
        continueWatchingDao.upsertContinueWatching(FAKE_CONTINUE_WATCHING_2)
        val result = continueWatchingDao.observeContinueWatching(TEST_USER_ID).first()

        // Then
        assertThat(result).contains(FAKE_CONTINUE_WATCHING_2)
    }

    @Test
    fun shouldReturnEmptyList_whenUserHasNoContinueWatchingRecords() = runBlocking {
        // When
        val result =
            continueWatchingDao.getContinueWatching(userId = 999L, pageSize = 10, offset = 0)

        // Then
        assertThat(result).isEmpty()
    }

    companion object {
        const val TEST_USER_ID = 100L

        val FAKE_CONTINUE_WATCHING_1 = ContinueWatching(
            contentId = 1L,
            userId = TEST_USER_ID,
            genreIds = listOf(1L),
            contentImageUrl = "url_1",
            contentType = "movie"
        )

        val FAKE_CONTINUE_WATCHING_2 = ContinueWatching(
            contentId = 2L,
            userId = TEST_USER_ID,
            genreIds = listOf(2L),
            contentImageUrl = "url_2",
            contentType = "series"
        )

        val FAKE_ITEMS = List(15) { index ->
            ContinueWatching(
                contentId = index.toLong(),
                userId = TEST_USER_ID,
                genreIds = listOf(1L, 2L),
                contentImageUrl = "image_url_$index",
                contentType = if (index % 2 == 0) "movie" else "series",
                viewedAt = index.toLong()
            )
        }
    }
}
