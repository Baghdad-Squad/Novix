package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserWatchedMediaTvShowsByGenreUseCaseTest {

    private lateinit var repository: UserWatchedMediaRepository
    private lateinit var useCase: GetUserWatchedMediaTvShowsByGenreUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetUserWatchedMediaTvShowsByGenreUseCase(repository)
    }

    @Test
    fun `invoke() should return filtered TvShows items matching genreId`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        val allItems = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )

        val pagedResult = PagedResult(
            data = allItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getPagedTvShows(page, 20) } returns pagedResult

        // When
        val result = useCase(genreId, page, 20)

        // Then
        assertThat(result.data).isEqualTo(listOf(allItems[0]))
        assertThat(result.nextKey).isEqualTo(pagedResult.nextKey)
        assertThat(result.prevKey).isEqualTo(pagedResult.prevKey)

        coVerify(exactly = 1) { repository.getPagedTvShows(page, 20) }
    }

    @Test
    fun `invoke() should return empty list if no item matches genreId`() = runTest {
        // Given
        val genreId = 99L
        val page = 1
        val allItems = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )

        val pagedResult = PagedResult(
            data = allItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getPagedTvShows(page, 20) } returns pagedResult

        // When
        val result = useCase(genreId, page, 20)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isEqualTo(pagedResult.nextKey)
        assertThat(result.prevKey).isEqualTo(pagedResult.prevKey)

        coVerify(exactly = 1) { repository.getPagedTvShows(page, 20) }
    }
}
