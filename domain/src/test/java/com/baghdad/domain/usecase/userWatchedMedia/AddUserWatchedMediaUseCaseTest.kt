package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddUserWatchedMediaUseCaseTest {

    private val repository: UserWatchedMediaRepository = mockk(relaxed = true)
    private val useCase: AddUserWatchedMediaUseCase =
        AddUserWatchedMediaUseCase(repository)

    @Test
    fun `invoke() should call repository with correct parameters`() = runTest {
        val userWatchedMedia = getUserWatchedMedia()

        useCase(
            contentId = userWatchedMedia.contentId,
            genreIds = userWatchedMedia.genreIds,
            contentImageUrl = userWatchedMedia.contentImageUrl,
            contentType = userWatchedMedia.contentType
        )

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
        val userWatchedMedia = getUserWatchedMedia()

        coEvery {
            repository.addUserWatchedMedia(any(), any(), any(), any())
        } returns Unit

        useCase(
            contentId = userWatchedMedia.contentId,
            genreIds = userWatchedMedia.genreIds,
            contentImageUrl = userWatchedMedia.contentImageUrl,
            contentType = userWatchedMedia.contentType
        )
    }
}
