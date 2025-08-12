package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.repository.ContinueWatchingRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddContinueWatchingUseCaseTest {

    private lateinit var repository: ContinueWatchingRepository
    private lateinit var useCase: AddContinueWatchingUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddContinueWatchingUseCase(repository)
    }

    @Test
    fun `invoke() should call repository with correct parameters`() = runTest {
        // Given
        val contentId = 101L
        val genreIds = listOf(1L, 2L, 3L)
        val contentImageUrl = "https://image.tmdb.org/t/p/w500/sample.jpg"
        val contentType = UserWatchedMedia.ContentType.MOVIE

        // When
        useCase(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType
        )

        // Then
        coVerify(exactly = 1) {
            repository.addContinueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType
            )
        }
    }

    @Test
    fun `invoke() should complete successfully when repository does not throw`() = runTest {
        // Given
        val contentId = 200L
        val genreIds = listOf(5L, 8L)
        val imageUrl = "https://image.tmdb.org/t/p/w500/test.jpg"
        val contentType = UserWatchedMedia.ContentType.TV_SHOW

        coEvery {
            repository.addContinueWatching(any(), any(), any(), any())
        } returns Unit

        // When & Then (no exception)
        useCase(contentId, genreIds, imageUrl, contentType)
    }
}
