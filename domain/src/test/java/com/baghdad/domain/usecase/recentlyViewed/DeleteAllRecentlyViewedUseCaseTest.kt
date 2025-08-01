package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteAllRecentlyViewedUseCaseTest {

    private lateinit var recentlyViewedRepository: RecentlyViewedRepository
    private lateinit var deleteAllRecentlyViewedUseCase: DeleteAllRecentlyViewedUseCase

    @BeforeEach
    fun setUp() {
        recentlyViewedRepository = mockk(relaxed = true)
        deleteAllRecentlyViewedUseCase = DeleteAllRecentlyViewedUseCase(recentlyViewedRepository)
    }

    @Test
    fun `invoke() should delete all recently viewed successfully`() = runTest {
        // When
        deleteAllRecentlyViewedUseCase()

        // Then
        coVerify(exactly = 1) { recentlyViewedRepository.deleteAllRecentlyViewed() }
    }

    @Test
    fun `invoke() should propagate exception when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException()
        coEvery { recentlyViewedRepository.deleteAllRecentlyViewed() } throws exception

        // When
        val resultException = runCatching { deleteAllRecentlyViewedUseCase() }.exceptionOrNull()

        // Then
        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}
