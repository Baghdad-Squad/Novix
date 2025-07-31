package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ContinueWatchingDto
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

    private lateinit var continueWatchingDao: ContinueWatchingDao
    private lateinit var logger: Logger
    private lateinit var dataSource: LocalContinueWatchingDataSourceImpl

    @BeforeEach
    fun setUp() {
        continueWatchingDao = mockk()
        logger = mockk(relaxed = true)
        dataSource = LocalContinueWatchingDataSourceImpl(continueWatchingDao, logger)
    }

    @Test
    fun `should add to continue watching when addContinueWatching is called`() = runTest {
        // Given
        coEvery { continueWatchingDao.upsertContinueWatching(CONTINUE_WATCHING.toLocalDto()) } returns Unit

        // When
        val result = dataSource.addContinueWatching(CONTINUE_WATCHING)

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should return continue watching list when getContinueWatching is called`() = runTest {
        // Given
        coEvery { continueWatchingDao.getContinueWatching(1, any(), any()) } returns listOf(
            CONTINUE_WATCHING.toLocalDto()
        )

        // When
        val result = dataSource.getContinueWatching(1, pageSize = 10, page = 1)

        // Then
        assertThat(result).isEqualTo(listOf(CONTINUE_WATCHING))
    }

    @Test
    fun `should observe continue watching when observeContinueWatching is called`() = runTest {
        // Given
        every { continueWatchingDao.observeContinueWatching(1) } returns flowOf(
            listOf(CONTINUE_WATCHING.toLocalDto())
        )

        // When
        val flow = dataSource.observeContinueWatching(1)
        val result = flow.first()

        // Then
        assertThat(result).isEqualTo(listOf(CONTINUE_WATCHING))
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
