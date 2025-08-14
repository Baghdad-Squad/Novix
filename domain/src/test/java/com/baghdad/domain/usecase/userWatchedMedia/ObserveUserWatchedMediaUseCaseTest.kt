package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ObserveUserWatchedMediaUseCaseTest {
    private val repository: UserWatchedMediaRepository = mockk()
    private val useCase: ObserveUserWatchedMediaUseCase =
        ObserveUserWatchedMediaUseCase(repository)

    @Test
    fun `should return flow of user watched media items when invoked`() = runTest {
        val userWatchedMediaItems = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )

        coEvery { repository.observeUserWatchedMedia() } returns flowOf(userWatchedMediaItems)

        val result = useCase()

        assertThat(result.first()).isEqualTo(userWatchedMediaItems)
    }
}