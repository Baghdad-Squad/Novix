package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddUserWatchedMediaUseCaseTest {

    private lateinit var repository: UserWatchedMediaRepository
    private lateinit var useCase: AddUserWatchedMediaUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = AddUserWatchedMediaUseCase(repository)
    }

    @Test
    fun `invoke() should call repository with correct parameters`() = runTest {
        // Given
        val userWatchedMedia = getUserWatchedMedia()

        // When
        useCase(
            contentId = userWatchedMedia.contentId,
            genreIds = userWatchedMedia.genreIds,
            contentImageUrl = userWatchedMedia.contentImageUrl,
            contentType = userWatchedMedia.contentType
        )

        // Then
        coVerify(exactly = 1) {
            repository.addUserWatchedMedia(
                contentId = userWatchedMedia.contentId,
                genreIds = userWatchedMedia.genreIds,
                contentImageUrl = userWatchedMedia.contentImageUrl,
                contentType = userWatchedMedia.contentType
            )
        }
    }

    @Test
    fun `invoke() should complete successfully when repository does not throw`() = runTest {
        // Given
        val userWatchedMedia = getUserWatchedMedia()


        coEvery {
            repository.addUserWatchedMedia(any(), any(), any(), any())
        } returns Unit

        // When & Then (no exception)
        useCase(
            contentId = userWatchedMedia.contentId,
            genreIds = userWatchedMedia.genreIds,
            contentImageUrl = userWatchedMedia.contentImageUrl,
            contentType = userWatchedMedia.contentType
        )
    }
}
