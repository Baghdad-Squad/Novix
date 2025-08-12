package com.baghdad.localDatasource

import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.localDatasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalContinueWatchingDataSourceImplTest {

    private lateinit var userWatchedMediaDao: UserWatchedMediaDao
    private lateinit var logger: Logger
    private lateinit var dataSource: LocalUserWatchedMediaDataSourceImpl

    @BeforeEach
    fun setUp() {
        userWatchedMediaDao = mockk()
        logger = mockk(relaxed = true)
        dataSource = LocalUserWatchedMediaDataSourceImpl(userWatchedMediaDao, logger)
    }

    @Test
    fun `should add to continue watching when addContinueWatching is called`() = runTest {
        coEvery { userWatchedMediaDao.upsertUserWatchedMedia(any()) } returns Unit

        val result = dataSource.addUserWatchedMedia(continueWatching)

        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should return continue watching list when getContinueWatching is called`() = runTest {
        coEvery { userWatchedMediaDao.getContinueWatching(1, any(), any()) } returns listOf(
            continueWatching.toLocalDto()
        )

        val result = dataSource.getContinueWatching(1, pageSize = 10, page = 1)

        assertThat(result).isEqualTo(listOf(continueWatching))
    }

    @Test
    fun `should observe continue watching when observeContinueWatching is called`() = runTest {
        every { userWatchedMediaDao.observeUserWatchedMedia(1) } returns flowOf(
            listOf(continueWatching.toLocalDto())
        )

        val flow = dataSource.observeUserWatchedMedia(1)
        val result = flow.first()

        assertThat(result).isEqualTo(listOf(continueWatching))
    }

    companion object {
        val continueWatching = UserWatchedMediaDto(
            contentId = 1,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "test",
            contentType = UserWatchedMediaDto.ContentType.MOVIE,
            userId = 1,
        )
    }
}
