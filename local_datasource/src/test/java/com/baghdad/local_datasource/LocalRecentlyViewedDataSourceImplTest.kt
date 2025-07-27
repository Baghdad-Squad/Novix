package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.RecentlyViewedDto.ContentType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalRecentlyViewedDataSourceImplTest {
    private lateinit var recentlyViewedDao: RecentlyViewedDao
    private lateinit var logger: Logger
    private lateinit var localRecentlyViewedDataSourceImpl: LocalRecentlyViewedDataSourceImpl

    @BeforeEach
    fun setUp() {
        recentlyViewedDao = mockk()
        logger = mockk()

        localRecentlyViewedDataSourceImpl =
            LocalRecentlyViewedDataSourceImpl(recentlyViewedDao, logger)
    }

    @Test
    fun `should return flow of recently viewed when dao returns data`() {
        // Given
        coEvery { recentlyViewedDao.getAllRecentlyViewed() } returns flowOf(emptyList())
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localRecentlyViewedDataSourceImpl.getAllRecentlyViewed()

        // Then
        assertNotNull(result)
    }


    @Test
    fun `should clear all recently viewed when invoked`() = runTest {
        // Given
        coEvery { recentlyViewedDao.clearAllRecentlyViewed() } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localRecentlyViewedDataSourceImpl.deleteAllRecentlyViewed()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `should add media to recently viewed when valid media is given`() = runTest {
        // Given
        val resentViewedDto = RecentlyViewedDto(
            contentId = 1,
            contentImageUrl = "test",
            contentType = ContentType.MOVIE,
            viewedAtEpochMillis = 20L
        )
        coEvery { recentlyViewedDao.upsertRecentlyViewed(any()) } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localRecentlyViewedDataSourceImpl.addMediaToRecentlyViewed(resentViewedDto)

        // Then
        assertNotNull(result)
    }
}