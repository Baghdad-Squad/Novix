package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.RecentSearchDao
import com.baghdad.local_datasource.database.dto.LocalRecentSearchDto
import com.baghdad.local_datasource.database.dto.toDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalSearchDataSourceImplTest {

    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var dataSource: LocalSearchDataSourceImpl


    @BeforeEach
    fun setup() {
        recentSearchDao = mockk(relaxed = true)
        dataSource = LocalSearchDataSourceImpl(recentSearchDao)
    }

    @Test
    fun `addRecentSearchQuery inserts dto with correct query`() = runTest {
        val query = "Tenet"

        coEvery { recentSearchDao.addRecentSearch(any()) } just Runs

        dataSource.addRecentSearchQuery(query)

        coVerify {
            recentSearchDao.addRecentSearch(withArg {
                Assertions.assertEquals("Tenet", it.query)
            })
        }
    }

    @Test
    fun `getAllRecentSearches returns sorted mapped results`() = runTest {
        val unsorted = listOf(fakeSearch1, fakeSearch2, fakeSearch3)
        val flow = flowOf(unsorted)

        coEvery { recentSearchDao.getAllRecentSearch() } returns flow

        val result = dataSource.getAllRecentSearches().first()

        val expected = listOf(fakeSearch2, fakeSearch3, fakeSearch1).map { it.toDto() }
        Assertions.assertEquals(expected, result)

    }

    @Test
    fun `deleteRecentSearchById removes item with correct id`() = runTest {
        val id = 99L
        coEvery { recentSearchDao.deleteRecentSearchById(id) } just Runs

        dataSource.deleteRecentSearchById(id)

        coVerify { recentSearchDao.deleteRecentSearchById(id) }
    }

    @Test
    fun `deleteAllRecentSearches calls clearAllRecentSearch on dao`() = runTest {
        coEvery { recentSearchDao.clearAllRecentSearch() } just Runs

        dataSource.deleteAllRecentSearches()

        coVerify { recentSearchDao.clearAllRecentSearch() }
    }


    val fakeSearch1 = LocalRecentSearchDto(
        id = 1L,
        query = "Blade Runner",
        searchedAt = 1700000000000L
    )

    val fakeSearch2 = LocalRecentSearchDto(
        id = 2L,
        query = "Dune",
        searchedAt = 1700000001000L
    )

    val fakeSearch3 = LocalRecentSearchDto(
        id = 3L,
        query = "Oppenheimer",
        searchedAt = 1700000000500L
    )

}