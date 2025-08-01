package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.RecentlyViewedDto.ContentType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    fun `should return flow of recently viewed when dao returns data`() = runTest {
        // Given
        val dto = RecentlyViewedDto(1, "url", ContentType.MOVIE, 123L)
        val entity = dto.toLocalDto()
        coEvery { recentlyViewedDao.getAllRecentlyViewed() } returns flowOf(listOf(entity))
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localRecentlyViewedDataSourceImpl.getAllRecentlyViewed().first()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0].contentId).isEqualTo(dto.contentId)
    }

    @Test
    fun `should clear all recently viewed when invoked`() = runTest {
        // Given
        coEvery { recentlyViewedDao.clearAllRecentlyViewed() } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localRecentlyViewedDataSourceImpl.deleteAllRecentlyViewed()

        // Then
        assertThat(result).isNotNull()
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
        assertThat(result).isNotNull()
    }
}