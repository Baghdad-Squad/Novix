package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.AppConfigurationsRepository
import com.baghdad.domain.usecase.appConfigurations.SetFirstTimeLaunchAppUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SetFirstTimeLaunchAppUseCaseTest {

    private val appConfigurationsRepository = mockk<AppConfigurationsRepository>()
    private val setFirstTimeLaunchAppUseCase =
        SetFirstTimeLaunchAppUseCase(appConfigurationsRepository)

    @Test
    fun `setFirstTimeLaunchAppUseCase should set first time user flag to true`() = runTest {
        coEvery { appConfigurationsRepository.setFirstTimeUser(any()) } just Runs

        setFirstTimeLaunchAppUseCase(true)

        coVerify { appConfigurationsRepository.setFirstTimeUser(firstTime = true) }
    }
}