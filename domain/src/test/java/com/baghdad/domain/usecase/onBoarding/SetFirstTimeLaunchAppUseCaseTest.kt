package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.OnBoardingRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
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

class SetFirstTimeLaunchAppUseCaseTest {
    private lateinit var useCase: SetFirstTimeLaunchAppUseCase
    private val repository: OnBoardingRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = SetFirstTimeLaunchAppUseCase(repository)
    }


    @Test
    fun `should set first time user flag to true`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } just Runs

        // When
        useCase(true)

        // Then
        coVerify { repository.setFirstTimeUser(firstTime = true) }
    }

    @Test
    fun `should set first time user flag to false`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } just Runs

        // When
        useCase(false)

        // Then
        coVerify { repository.setFirstTimeUser(firstTime = false) }
    }

    @Test
    fun `should propagate exceptions from repository`() = runTest {
        // Given
        val expectedException = RuntimeException("Test error")
        coEvery { repository.setFirstTimeUser(any()) } throws expectedException

        // When/Then
        assertThrows<RuntimeException> {
            useCase(true)
        }.apply {
            assertThat(this).isSameInstanceAs(expectedException)
        }
    }


    @Test
    fun `should complete successfully when repository operation succeeds`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } just Runs

        // When
        val result = runCatching { useCase(true) }

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should complete successfully with multiple consecutive calls`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } just Runs

        // When
        useCase(true)
        useCase(false)
        useCase(true)

        // Then
        coVerify(exactly = 3) { repository.setFirstTimeUser(any()) }
        coVerifyOrder {
            repository.setFirstTimeUser(firstTime = true)
            repository.setFirstTimeUser(firstTime = false)
            repository.setFirstTimeUser(firstTime = true)
        }
    }

    @Test
    fun `should handle repository cancellation`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } coAnswers {
            delay(100) // Simulate some work
            throw CancellationException("Test cancellation")
        }

        // When/Then
        assertThrows<CancellationException> {
            useCase(true)
        }
    }

    @Test
    fun `should not complete when repository hangs`() = runTest {
        // Given
        coEvery { repository.setFirstTimeUser(any()) } coAnswers {
            delay(Long.MAX_VALUE) // Simulate infinite hang
        }

        // When
        val job = launch {
            useCase(true)
        }

        // Then
        delay(100) // Wait a bit
        assertThat(job.isActive).isTrue()
        job.cancel()
    }


    @Test
    fun `should verify coroutine context`() = runTest {
        // Given
        var capturedContext: CoroutineContext? = null
        coEvery { repository.setFirstTimeUser(any()) } coAnswers {
            capturedContext = coroutineContext
        }

        // When
        useCase(true)

        // Then
        assertThat(capturedContext).isNotNull()
        assertThat(capturedContext?.get(Job)).isNotNull()
    }
}