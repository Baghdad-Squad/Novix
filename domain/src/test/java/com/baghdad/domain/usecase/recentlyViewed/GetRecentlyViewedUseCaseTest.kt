package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.testHelper.getRecentlyViewedList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetRecentlyViewedUseCaseTest {

    private lateinit var recentlyViewedRepository: RecentlyViewedRepository
    private lateinit var getRecentlyViewedUseCase: GetRecentlyViewedUseCase

    @BeforeEach
    fun setUp() {
        recentlyViewedRepository = mockk(relaxed = true)
        getRecentlyViewedUseCase = GetRecentlyViewedUseCase(recentlyViewedRepository)
    }

    @Test
    fun `returns all items sorted by viewedAt when repository emits items`() = runTest {
        val items = getRecentlyViewedList(15).shuffled()
        val expected = items.sortedByDescending { it.viewedAt }

        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(items)

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(15)
        assertThat(result.first()).containsExactlyElementsIn(expected).inOrder()
    }

    @Test
    fun `returns empty list when repository emits empty list`() = runTest {
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(emptyList())

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `applies sorting to each emission when repository emits multiple lists`() = runTest {
        val firstEmission = getRecentlyViewedList(5).shuffled()
        val secondEmission = getRecentlyViewedList(8).shuffled()

        val expectedFirst = firstEmission.sortedByDescending { it.viewedAt }
        val expectedSecond = secondEmission.sortedByDescending { it.viewedAt }

        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(
            firstEmission,
            secondEmission
        )

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(2)
        assertThat(result[0]).containsExactlyElementsIn(expectedFirst).inOrder()
        assertThat(result[1]).containsExactlyElementsIn(expectedSecond).inOrder()
    }

    @Test
    fun `throws exception when repository throws`() = runTest {
        val exception = RuntimeException()
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } throws exception

        val resultException = runCatching { getRecentlyViewedUseCase().toList() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}