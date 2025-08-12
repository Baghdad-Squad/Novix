package com.baghdad.localDatasource

import com.baghdad.localDatasource.mapper.toDto
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.entity.RecentSearch
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
import org.junit.jupiter.api.Test

class LocalRecentSearchDataSourceImplTest {
    private var recentSearchDao: RecentSearchDao = mockk()
    private var logger: Logger = mockk(relaxed = true)
    private var localSearchDataSourceImpl: LocalRecentSearchDataSourceImpl =
        LocalRecentSearchDataSourceImpl(recentSearchDao = recentSearchDao, logger = logger)

    @Test
    fun `addRecentSearchQuery should save recent search query to database when it invoked`() =
        runTest {
            val query = "fast and furious"
            coEvery { recentSearchDao.getRecentSearchByQuery(query) } returns null
            coEvery { recentSearchDao.upsertRecentSearch(any()) } just Runs

            localSearchDataSourceImpl.addRecentSearchQuery(query)

            coVerify(exactly = 1) { recentSearchDao.upsertRecentSearch(any()) }
        }

    @Test
    fun `getAllRecentSearches should return all recent search queries when it invoked`() = runTest {
        val recentSearchesDTOs = recentSearches.map { it.toDto() }
        coEvery { recentSearchDao.getAllRecentSearch() } returns flowOf(recentSearches)

        val result = localSearchDataSourceImpl.getAllRecentSearches().first()

        assertThat(result).isEqualTo(recentSearchesDTOs)
    }

    @Test
    fun `deleteRecentSearchById should delete recent search by id when it invoked`() = runTest {
        val id = 1L
        coEvery { recentSearchDao.deleteRecentSearchById(id) } just Runs

        localSearchDataSourceImpl.deleteRecentSearchById(id)

        coVerify(exactly = 1) { recentSearchDao.deleteRecentSearchById(id) }
    }

    @Test
    fun `deleteAllRecentSearches should delete all recent search queries when it invoked`() =
        runTest {
            coEvery { recentSearchDao.clearAllRecentSearch() } just Runs

            localSearchDataSourceImpl.deleteAllRecentSearches()

            coVerify { recentSearchDao.clearAllRecentSearch() }
        }

    private companion object {
        val recentSearches = listOf(
            RecentSearch(query = "spongebob"),
            RecentSearch(query = "tom and jerry"),
            RecentSearch(query = "ben 10")
        )
    }
}