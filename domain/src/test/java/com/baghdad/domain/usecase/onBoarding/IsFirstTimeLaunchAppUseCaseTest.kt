package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.AppConfigurationsRepository
import com.baghdad.domain.usecase.appConfigurations.IsFirstTimeLaunchAppUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class IsFirstTimeLaunchAppUseCaseTest {

    private val appConfigurationsRepository: AppConfigurationsRepository = mockk()
    private var isFirstTimeLaunchAppUseCase =
        IsFirstTimeLaunchAppUseCase(appConfigurationsRepository)

    @Test
    fun `isFirstTimeLaunchAppUseCase should return true when user is first time`() = runTest {
        coEvery { appConfigurationsRepository.isFirstTimeUser() } returns true

        val result = isFirstTimeLaunchAppUseCase()

        assertThat(result).isTrue()
    }
}