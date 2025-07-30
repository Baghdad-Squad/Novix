package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ContinueWatchingDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalContinueWatchingDataSourceImplTest {

    lateinit var continueWatchingDao: ContinueWatchingDao
    lateinit var logger: Logger

    lateinit var localContinueWatchingDataSourceImpl: LocalContinueWatchingDataSourceImpl

    @BeforeEach
    fun setUp() {
        continueWatchingDao = mockk()
        logger = mockk()

        localContinueWatchingDataSourceImpl =
            LocalContinueWatchingDataSourceImpl(continueWatchingDao, logger)
    }

    @Test
    fun `should add to continue watching when call addContinueWatching`() = runTest {
        // Given
        coEvery { continueWatchingDao.upsertContinueWatching(CONTINUE_WATCHING.toLocalDto()) } returns Unit
        coEvery { logger.logException(any()) } returns Unit
        coEvery { continueWatchingDao.upsertContinueWatching(any()) } returns Unit

        // When
        val result = localContinueWatchingDataSourceImpl.addContinueWatching(CONTINUE_WATCHING)

        // Then
        assert(result == Unit)
    }

    @Test
    fun `should get continue watching when call getContinueWatching`() = runTest {
        // Given
        coEvery { continueWatchingDao.getContinueWatching(1, any(), any()) } returns listOf(
            CONTINUE_WATCHING.toLocalDto()
        )
        coEvery { logger.logException(any()) } returns Unit
        // When
        val result = localContinueWatchingDataSourceImpl.getContinueWatching(1, 10, 0)
        // Then
        assert(result == listOf(CONTINUE_WATCHING))
    }

    companion object {
        val CONTINUE_WATCHING = ContinueWatchingDto(
            contentId = 1,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "test",
            contentType = ContinueWatchingDto.ContentType.MOVIE,
            userId = 1
        )
    }

}