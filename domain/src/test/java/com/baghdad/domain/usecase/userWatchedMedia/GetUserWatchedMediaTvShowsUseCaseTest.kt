package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetUserWatchedMediaTvShowsUseCaseTest {
    private val repository: UserWatchedMediaRepository = mockk()
    private val useCase: GetUserWatchedMediaTvShowsUseCase =
        GetUserWatchedMediaTvShowsUseCase(repository)

    @Test
    fun `invoke() should return paged TvShows result from repository`() = runTest {
        val page = 1
        val pageSize = 20
        val expectedData = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )
        val expectedResult = PagedResult(
            data = expectedData,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getPagedTvShows(page, pageSize) } returns expectedResult

        val result = useCase(page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getPagedTvShows(page, pageSize) }
    }

    @Test
    fun `invoke() should return empty list when no data exists`() = runTest {
        val page = 5
        val pageSize = 20
        val expectedResult = PagedResult<UserWatchedMedia>(
            data = emptyList(),
            nextKey = null,
            prevKey = 4
        )

        coEvery { repository.getPagedTvShows(page, pageSize) } returns expectedResult

        val result = useCase(page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getPagedTvShows(page, pageSize) }
    }
}