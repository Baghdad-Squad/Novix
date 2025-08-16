package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.AppConfigurationsRepository
import com.baghdad.domain.usecase.appConfigurations.IsFirstTimeLaunchAppUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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

class IsFirstTimeLaunchAppUseCaseTest {
    private lateinit var useCase: IsFirstTimeLaunchAppUseCase
    private val repository: AppConfigurationsRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = IsFirstTimeLaunchAppUseCase(repository)
    }



    @Test
    fun `should return true when user is first time`() = runTest {
        // Given
        coEvery { repository.isFirstTimeUser() } returns true

        // When
        val result = useCase()

        // Then
        assertThat(result).isTrue()
        coVerify { repository.isFirstTimeUser() }
    }

    @Test
    fun `should return false when user is not first time`() = runTest {
        // Given
        coEvery { repository.isFirstTimeUser() } returns false

        // When
        val result = useCase()

        // Then
        assertThat(result).isFalse()
        coVerify { repository.isFirstTimeUser() }
    }

    @Test
    fun `should propagate repository exceptions`() = runTest {
        // Given
        val expectedException = RuntimeException("DB error")
        coEvery { repository.isFirstTimeUser() } throws expectedException

        // When/Then
        assertThrows<RuntimeException> {
            useCase()
        }.apply {
            assertThat(this).isSameInstanceAs(expectedException)
        }
    }

    @Test
    fun `should complete successfully when repository operation succeeds`() = runTest {
        // Given
        coEvery { repository.isFirstTimeUser() } returns true

        // When
        val result = runCatching { useCase() }

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
    }

    @Test
    fun `should handle repository cancellation`() = runTest {
        // Given
        coEvery { repository.isFirstTimeUser() } coAnswers {
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
        coEvery { repository.isFirstTimeUser() } coAnswers {
            capturedContext = coroutineContext
            true
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
        coEvery { repository.isFirstTimeUser() } coAnswers {
            delay(Long.MAX_VALUE)
            true
        }

        // When
        val job = launch {
            useCase()
        }

        // Then
        delay(100)
        assertThat(job.isActive)
        job.cancel()
    }


}