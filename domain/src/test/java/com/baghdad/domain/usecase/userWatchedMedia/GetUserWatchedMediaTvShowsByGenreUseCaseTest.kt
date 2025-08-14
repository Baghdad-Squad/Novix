package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetUserWatchedMediaTvShowsByGenreUseCaseTest {

    private val repository: UserWatchedMediaRepository = mockk()
    private val useCase: GetUserWatchedMediaTvShowsByGenreUseCase =
        GetUserWatchedMediaTvShowsByGenreUseCase(repository)

    @Test
    fun `invoke() should return filtered TvShows items matching genreId`() = runTest {
        val genreId = 1L

        val pagedResult = PagedResult(
            data = userWatchMediaItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getPagedTvShows(page, pageSize) } returns pagedResult

        val result = useCase(genreId, page, pageSize)

        assertThat(result.data).isEqualTo(listOf(userWatchMediaItems[0]))
        assertThat(result.nextKey).isEqualTo(pagedResult.nextKey)
        assertThat(result.prevKey).isEqualTo(pagedResult.prevKey)

        coVerify(exactly = 1) { repository.getPagedTvShows(page, pageSize) }
    }

    @Test
    fun `invoke() should return empty list if no item matches genreId`() = runTest {
        val genreId = 99L

        val pagedResult = PagedResult(
            data = userWatchMediaItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getPagedTvShows(page, pageSize) } returns pagedResult

        val result = useCase(genreId, page, pageSize)

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isEqualTo(pagedResult.nextKey)
        assertThat(result.prevKey).isEqualTo(pagedResult.prevKey)

        coVerify(exactly = 1) { repository.getPagedTvShows(page, pageSize) }
    }

    private companion object {
        val userWatchMediaItems = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )
        val page = 1
        val pageSize = 20
    }
}
