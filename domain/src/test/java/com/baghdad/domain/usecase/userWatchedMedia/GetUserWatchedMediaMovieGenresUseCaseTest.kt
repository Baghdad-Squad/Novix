package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserWatchedMediaMovieGenresUseCaseTest {
    private lateinit var repository: UserWatchedMediaRepository
    private lateinit var useCase: GetUserWatchedMediaMovieGenresUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = GetUserWatchedMediaMovieGenresUseCase(repository)
    }

    @Test
    fun `should return flow of movie genres when invoked`() = runTest {
        // Given
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy"),
            Genre(3, "Drama")
        )
        coEvery { repository.getUsedMovieGenres() } returns flowOf(genres)

        // When
        val result = useCase()

        // Then
        assertThat(result.first()).isEqualTo(genres)
    }
}