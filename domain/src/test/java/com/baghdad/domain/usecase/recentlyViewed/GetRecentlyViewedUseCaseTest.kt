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
    fun `should return flow with limited to 10 sorted items when repository returns more than 10`() =
        runTest {
            val items = getRecentlyViewedList(15).shuffled()
            val expected = items.sortedByDescending { it.viewedAt }.take(10)
            coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(items)

            val result = getRecentlyViewedUseCase().toList()

            assertThat(result).hasSize(1)
            assertThat(result.first()).hasSize(10)
            assertThat(result.first()).containsExactlyElementsIn(expected).inOrder()
        }

    @Test
    fun `should return flow with all sorted items when repository returns less than 10`() =
        runTest {
            val items = getRecentlyViewedList(3).shuffled()
            val expected = items.sortedByDescending { it.viewedAt }
            coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(items)

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(3)
            assertThat(result.first()).containsExactlyElementsIn(expected).inOrder()
    }

    @Test
    fun `should return flow with exactly 10 sorted items when repository returns exactly 10`() =
        runTest {
            val items = getRecentlyViewedList(10).shuffled()
            val expected = items.sortedByDescending { it.viewedAt }
            coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(items)

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(10)
            assertThat(result.first()).containsExactlyElementsIn(expected).inOrder()
    }

    @Test
    fun `should return flow with empty list when repository returns empty list`() = runTest {
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(emptyList())

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should handle multiple flow emissions and sort+limit each one`() = runTest {
        val firstEmission = getRecentlyViewedList(1).shuffled()
        val secondEmission = getRecentlyViewedList(15).shuffled()
        val expectedFirst = firstEmission.sortedByDescending { it.viewedAt }.take(10)
        val expectedSecond = secondEmission.sortedByDescending { it.viewedAt }.take(10)

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
    fun `should propagate exception when repository throws exception`() = runTest {
        val exception = RuntimeException("Something went wrong")
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } throws exception

        val resultException = runCatching { getRecentlyViewedUseCase().toList() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}