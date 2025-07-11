package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.testHelper.getTestMedia
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
    fun `should return flow with limited to 10 items when repository returns more than 10`() =
        runTest {
            val mediaList = getTestMedia(15)
            coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(mediaList)

            val result = getRecentlyViewedUseCase().toList()

            assertThat(result).hasSize(1)
            assertThat(result.first()).hasSize(10)
            assertThat(result.first()).containsExactlyElementsIn(mediaList.take(10)).inOrder()
        }

    @Test
    fun `should return flow with all items when repository returns less than 10`() = runTest {
        val mediaList = getTestMedia(3)
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(mediaList)

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(3)
        assertThat(result.first()).containsExactlyElementsIn(mediaList).inOrder()
    }

    @Test
    fun `should return flow with exactly 10 items when repository returns exactly 10`() = runTest {
        val mediaList = getTestMedia(10)
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(mediaList)

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).hasSize(10)
        assertThat(result.first()).containsExactlyElementsIn(mediaList).inOrder()
    }

    @Test
    fun `should return flow with empty list when repository returns empty list`() = runTest {
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(emptyList())

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should handle multiple flow emissions from repository`() = runTest {
        val firstEmission = getTestMedia(1)
        val secondEmission = getTestMedia(15)
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } returns flowOf(
            firstEmission,
            secondEmission
        )

        val result = getRecentlyViewedUseCase().toList()

        assertThat(result).hasSize(2)
        assertThat(result[0]).hasSize(1)
        assertThat(result[1]).hasSize(10)
        assertThat(result[1]).containsExactlyElementsIn(secondEmission.take(10)).inOrder()
    }

    @Test
    fun `should propagate exception when repository throws exception`() = runTest {
        val exception = RuntimeException()
        coEvery { recentlyViewedRepository.getAllRecentlyViewed() } throws exception

        val resultException = runCatching { getRecentlyViewedUseCase().toList() }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}