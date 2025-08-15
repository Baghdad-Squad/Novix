package com.baghdad.localDatasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
import com.baghdad.localDatasource.roomDB.entity.UserWatchedMedia
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserWatchedMediaDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var userWatchedMediaDao: UserWatchedMediaDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()
        userWatchedMediaDao = database.userWatchedMediaDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun shouldInsertItemCorrectly_whenUpsertUserWatchedMediaIsCalled() = runBlocking {
        val pageSize = 1
        val offset = 0
        userWatchedMediaDao.upsertUserWatchedMedia(FAKE_USER_WATCHED_MEDIA_1)

        val result =
            userWatchedMediaDao.getPagedUserWatchedMediaMovies(TEST_USER_ID, pageSize, offset)

        assertThat(result.first()).isEqualTo(FAKE_USER_WATCHED_MEDIA_1)
    }

    @Test
    fun shouldUpdateItem_whenUpsertUserWatchedMediaCalledWithSameContentId() = runBlocking {
        val pageSize = 1
        val offset = 0
        userWatchedMediaDao.upsertUserWatchedMedia(FAKE_USER_WATCHED_MEDIA_1)

        val updated = FAKE_USER_WATCHED_MEDIA_1.copy(contentImageUrl = "new_url")
        userWatchedMediaDao.upsertUserWatchedMedia(updated)
        val result =
            userWatchedMediaDao.getPagedUserWatchedMediaMovies(TEST_USER_ID, pageSize, offset)

        assertThat(result.first().contentImageUrl).isEqualTo("new_url")
    }

    @Test
    fun shouldReturnPaginatedResultsInDescendingOrder_whenGetUserWatchedMediaIsCalled() =
        runBlocking {
            val pageSize = 5
            val offset = 5
            FAKE_ITEMS_MOVIES.forEach { userWatchedMediaDao.upsertUserWatchedMedia(it) }

            val page =
                userWatchedMediaDao.getPagedUserWatchedMediaMovies(
                    TEST_USER_ID,
                    pageSize = pageSize,
                    offset = offset
                )

            assertThat(page.size).isEqualTo(pageSize)
            assertThat(page.map { it.contentId }).isEqualTo(listOf(9L, 8L, 7L, 6L, 5L))
        }

    @Test
    fun shouldObserveUserWatchedMedia_whenNewItemIsInserted() = runBlocking {
        userWatchedMediaDao.upsertUserWatchedMedia(FAKE_USER_WATCHED_MEDIA_2)
        val result = userWatchedMediaDao.observeUserWatchedMedia(TEST_USER_ID).first()

        assertThat(result).contains(FAKE_USER_WATCHED_MEDIA_2)
    }

    @Test
    fun shouldReturnEmptyList_whenUserHasNoUserWatchedMediaRecords() = runBlocking {
        val result =
            userWatchedMediaDao.getPagedUserWatchedMediaMovies(
                userId = 999L,
                pageSize = 10,
                offset = 0
            )

        assertThat(result).isEmpty()
    }

    companion object {
        const val TEST_USER_ID = 100L

        val FAKE_USER_WATCHED_MEDIA_1 = UserWatchedMedia(
            contentId = 1L,
            userId = TEST_USER_ID,
            genreIds = listOf(1L),
            contentImageUrl = "url_1",
            contentType = "MOVIE"
        )

        val FAKE_USER_WATCHED_MEDIA_2 = UserWatchedMedia(
            contentId = 2L,
            userId = TEST_USER_ID,
            genreIds = listOf(2L),
            contentImageUrl = "url_2",
            contentType = "TV_SHOW"
        )

        val FAKE_ITEMS_MOVIES = List(15) { index ->
            UserWatchedMedia(
                contentId = index.toLong(),
                userId = TEST_USER_ID,
                genreIds = listOf(1L, 2L),
                contentImageUrl = "image_url_$index",
                contentType = "MOVIE",
                viewedAt = index.toLong()
            )
        }
    }
}
