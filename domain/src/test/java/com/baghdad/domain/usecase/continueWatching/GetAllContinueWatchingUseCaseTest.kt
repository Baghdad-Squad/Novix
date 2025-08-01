package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllContinueWatchingUseCaseTest {

    private lateinit var repository: ContinueWatchingRepository
    private lateinit var useCase: GetAllContinueWatchingUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetAllContinueWatchingUseCase(repository)
    }

    @Test
    fun `invoke() should return paged result from repository`() = runTest {
        // Given
        val page = 1
        val expectedData = listOf(
            ContinueWatching(
                contentId = 1L,
                genreIds = listOf(1, 2),
                contentImageUrl = "image_url_1",
                contentType = ContinueWatching.ContentType.MOVIE,
                userId = 100
            ),
            ContinueWatching(
                contentId = 2L,
                genreIds = listOf(3),
                contentImageUrl = "image_url_2",
                contentType = ContinueWatching.ContentType.TV_SHOW,
                userId = 101
            )
        )
        val expectedResult = PagedResult(
            data = expectedData,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getContinueWatching(page, 20) } returns expectedResult

        // When
        val result = useCase(page)

        // Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getContinueWatching(page, 20) }
    }

    @Test
    fun `invoke() should return empty list when no data exists`() = runTest {
        // Given
        val page = 5
        val expectedResult = PagedResult<ContinueWatching>(
            data = emptyList(),
            nextKey = null,
            prevKey = 4
        )

        coEvery { repository.getContinueWatching(page, 20) } returns expectedResult

        // When
        val result = useCase(page)

        // Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { repository.getContinueWatching(page, 20) }
    }
}
