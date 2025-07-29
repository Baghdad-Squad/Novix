package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllContinueWatchingByGenreUseCaseTest {

    private lateinit var repository: ContinueWatchingRepository
    private lateinit var useCase: GetAllContinueWatchingByGenreUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetAllContinueWatchingByGenreUseCase(repository)
    }

    @Test
    fun `invoke should return filtered items matching genreId`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        val allItems = listOf(
            ContinueWatching(
                contentId = 1,
                genreIds = listOf(1, 2),
                contentImageUrl = "url1",
                contentType = ContinueWatching.ContentType.MOVIE,
                userId = 101
            ),
            ContinueWatching(
                contentId = 2,
                genreIds = listOf(3),
                contentImageUrl = "url2",
                contentType = ContinueWatching.ContentType.TV_SHOW,
                userId = 102
            )
        )

        val pagedResult = PagedResult(
            data = allItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getContinueWatching(page, 20) } returns pagedResult

        // When
        val result = useCase(genreId, page)

        // Then
        assertEquals(1, result.data.size)
        assertEquals(1L, result.data.first().contentId)
        assertEquals(pagedResult.nextKey, result.nextKey)
        assertEquals(pagedResult.prevKey, result.prevKey)

        coVerify(exactly = 1) { repository.getContinueWatching(page, 20) }
    }

    @Test
    fun `invoke should return empty list if no item matches genreId`() = runTest {
        // Given
        val genreId = 99L
        val page = 1
        val allItems = listOf(
            ContinueWatching(
                contentId = 1,
                genreIds = listOf(1, 2),
                contentImageUrl = "url1",
                contentType = ContinueWatching.ContentType.MOVIE,
                userId = 101
            ),
            ContinueWatching(
                contentId = 2,
                genreIds = listOf(2, 3),
                contentImageUrl = "url2",
                contentType = ContinueWatching.ContentType.TV_SHOW,
                userId = 102
            )
        )

        val pagedResult = PagedResult(
            data = allItems,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getContinueWatching(page, 20) } returns pagedResult

        // When
        val result = useCase(genreId, page)

        // Then
        assertEquals(0, result.data.size)
        assertEquals(pagedResult.nextKey, result.nextKey)
        assertEquals(pagedResult.prevKey, result.prevKey)

        coVerify(exactly = 1) { repository.getContinueWatching(page, 20) }
    }
}
