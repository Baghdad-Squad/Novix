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

class GetUserWatchedMediaMoviesUseCaseTest {

    private val repository: UserWatchedMediaRepository = mockk()
    private val useCase: GetUserWatchedMediaMoviesUseCase =
        GetUserWatchedMediaMoviesUseCase(repository)

    @Test
    fun `invoke() should return paged result from repository`() = runTest {
        val page = 1
        val expectedData = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )
        val pageSize = 20
        val expectedResult = PagedResult(
            data = expectedData,
            nextPage = 2,
            prevPage = null
        )

        coEvery { repository.getPagedMovies(page, pageSize) } returns expectedResult

        val result = useCase(page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getPagedMovies(page, pageSize) }
    }

    @Test
    fun `invoke() should return empty list when no data exists`() = runTest {
        val page = 5
        val pageSize = 20
        val expectedResult = PagedResult<UserWatchedMedia>(
            data = emptyList(),
            nextPage = null,
            prevPage = 4
        )

        coEvery { repository.getPagedMovies(page, pageSize) } returns expectedResult

        val result = useCase(page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getPagedMovies(page, pageSize) }
    }
}
