package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.OnBoardingRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class ClearFirstTimeLaunchAppUseCaseTest {
    private lateinit var useCase: ClearFirstTimeLaunchAppUseCase
    private val repository: OnBoardingRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = ClearFirstTimeLaunchAppUseCase(repository)
    }

    @Test
    fun `should clear first time user flag`() = runTest {
        // Given
        coEvery { repository.clearFirstTimeUser() } just Runs

        // When
        useCase()

        // Then
        coVerify { repository.clearFirstTimeUser() }
    }

    @Test
    fun `should complete successfully when repository operation succeeds`() = runTest {
        // Given
        coEvery { repository.clearFirstTimeUser() } just Runs

        // When
        val result = runCatching { useCase() }

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should propagate repository exceptions`() = runTest {
        // Given
        val expectedException = RuntimeException("DB error")
        coEvery { repository.clearFirstTimeUser() } throws expectedException

        // When/Then
        assertThrows<RuntimeException> {
            useCase()
        }.apply {
            assertThat(this).isSameInstanceAs(expectedException)
        }
    }

    @Test
    fun `should handle repository cancellation`() = runTest {
        // Given
        coEvery { repository.clearFirstTimeUser() } coAnswers {
            delay(100) // Simulate some work
            throw CancellationException("Test cancellation")
        }

        // When/Then
        assertThrows<CancellationException> {
            useCase()
        }
    }

    @Test
    fun `should verify coroutine context`() = runTest {
        // Given
        var capturedContext: CoroutineContext? = null
        coEvery { repository.clearFirstTimeUser() } coAnswers {
            capturedContext = coroutineContext
        }

        // When
        useCase()

        // Then
        assertThat(capturedContext).isNotNull()
        assertThat(capturedContext?.get(Job)).isNotNull()
    }

    @Test
    fun `should not complete when repository hangs`() = runTest {
        // Given
        coEvery { repository.clearFirstTimeUser() } coAnswers {
            delay(Long.MAX_VALUE) // Simulate infinite hang
        }

        // When
        val job = launch {
            useCase()
        }

        // Then
        delay(100) // Wait a bit
        assertThat(job.isActive).isTrue()
        job.cancel()
    }

    @Test
    fun `should complete successfully with multiple consecutive calls`() = runTest {
        // Given
        coEvery { repository.clearFirstTimeUser() } just Runs

        // When
        useCase()
        useCase()
        useCase()

        // Then
        coVerify(exactly = 3) { repository.clearFirstTimeUser() }
    }
}