package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.testHelper.getUserWatchedMedia
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ObserveUserWatchedMediaUseCaseTest {
    private lateinit var repository: UserWatchedMediaRepository
    private lateinit var useCase: ObserveUserWatchedMediaUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = ObserveUserWatchedMediaUseCase(repository)
    }

    @Test
    fun `should return flow of user watched media items when invoked`() = runTest {
        // Given
        val userWatchedMediaItems = listOf(
            getUserWatchedMedia().copy(genreIds = listOf(1, 2)),
            getUserWatchedMedia().copy(genreIds = listOf(3))
        )

        coEvery { repository.observeUserWatchedMedia() } returns flowOf(userWatchedMediaItems)

        // When
        val result = useCase()

        // Then
        assertThat(result.first()).isEqualTo(userWatchedMediaItems)
    }
}