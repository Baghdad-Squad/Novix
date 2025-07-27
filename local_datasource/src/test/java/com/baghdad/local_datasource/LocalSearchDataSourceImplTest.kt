package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.repository.logger.Logger
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalSearchDataSourceImplTest {
    lateinit var recentSearchDao: RecentSearchDao
    lateinit var logger: Logger

    lateinit var localSearchDataSourceImpl: LocalSearchDataSourceImpl

    @BeforeEach
    fun setUp() {
        recentSearchDao = mockk()
        logger = mockk()

        localSearchDataSourceImpl =
            LocalSearchDataSourceImpl(recentSearchDao = recentSearchDao, logger = logger)
    }

    @Test
    fun `should save recent search query to database when addRecentSearchQuery is called`() =
        runTest {
            // Given
            val query = "test"
            coEvery { recentSearchDao.addRecentSearch(any()) } just Runs
            coEvery { logger.logException(any()) } just Runs

            // When
            localSearchDataSourceImpl.addRecentSearchQuery(query)

            // Then
            coVerify { recentSearchDao.addRecentSearch(match { it.query == query }) }
        }

    @Test
    fun `should return all recent search queries when getAllRecentSearches is called`() = runTest {
        // Given
        val recentSearches = listOf(
            RecentSearch(query = "test1"),
            RecentSearch(query = "test2"),
            RecentSearch(query = "test3")
        )
        val expectedDtos = recentSearches.map { it.toDto() }

        coEvery { recentSearchDao.getAllRecentSearch() } returns flowOf(recentSearches)
        coEvery { logger.logException(any()) } just Runs

        // When
        val result = localSearchDataSourceImpl.getAllRecentSearches().first()

        // Then
        coVerify { recentSearchDao.getAllRecentSearch() }
        assertThat(result).isEqualTo(expectedDtos)
    }

    @Test
    fun `should delete recent search by id when deleteRecentSearchById is called`() = runTest {
        // Given
        val id = 1L
        coEvery { recentSearchDao.deleteRecentSearchById(id) } just Runs

        // When
        localSearchDataSourceImpl.deleteRecentSearchById(id)

        // Then
        coVerify { recentSearchDao.deleteRecentSearchById(id) }
    }

    @Test
    fun `should delete all recent search queries when deleteAllRecentSearches is called`() =
        runTest {
            // Given
            coEvery { recentSearchDao.clearAllRecentSearch() } just Runs
            coEvery { logger.logException(any()) } just Runs

            // When
            localSearchDataSourceImpl.deleteAllRecentSearches()

            // Then
            coVerify { recentSearchDao.clearAllRecentSearch() }
        }

}