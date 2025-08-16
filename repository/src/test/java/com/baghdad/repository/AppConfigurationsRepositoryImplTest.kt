package com.baghdad.repository

import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.Locale

class AppConfigurationsRepositoryImplTest {
    private val appConfigurationDataSource: AppConfigurationDataSource = mockk()
    private val repository = AppConfigurationsRepositoryImpl(appConfigurationDataSource)

    @Test
    fun `should return true when isAppInDarkTheme returns true`() = runTest {
        coEvery { appConfigurationDataSource.isAppInDarkTheme() } returns flowOf(true)

        val result = repository.isAppInDarkTheme()

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `should return false when isAppInDarkTheme returns false`() = runTest {
        coEvery { appConfigurationDataSource.isAppInDarkTheme() } returns flowOf(false)

        val result = repository.isAppInDarkTheme()

        assertThat(result.first()).isFalse()
    }

    @Test
    fun `setDarkTheme should enables dark theme when enabled is true`() = runTest {
        val enable = true
        coEvery { appConfigurationDataSource.setDarkTheme(enable) } returns Unit

        repository.setDarkTheme(enable)

        coVerify { appConfigurationDataSource.setDarkTheme(enable) }
    }

    @Test
    fun `should setDarkTheme disables dark theme when enabled is false`() = runTest {
        val enable = false
        coEvery { appConfigurationDataSource.setDarkTheme(enable) } returns Unit

        repository.setDarkTheme(enable)

        coVerify { appConfigurationDataSource.setDarkTheme(enable) }
    }

    @Test
    fun `getAppLanguage should returns existing language when data source returns existing language`() =
        runTest {
            coEvery { appConfigurationDataSource.getAppLanguage() } returns flowOf("en")

            val result = repository.getAppLanguage()

            assertThat(result.first()).isEqualTo("en")
        }

    @Test
    fun `getAppLanguage should returns default language when data source returns null`() = runTest {
        coEvery { appConfigurationDataSource.getAppLanguage() } returns flowOf(null)
        coEvery { appConfigurationDataSource.setAppLanguage(any()) } returns Unit

        val result = repository.getAppLanguage()

        assertThat(result.first()).isEqualTo(Locale.getDefault().language)
    }

    @Test
    fun `setAppLanguage should sets a valid language when language is valid`() = runTest {
        val language = "en"
        coEvery { appConfigurationDataSource.setAppLanguage(language) } returns Unit

        repository.setAppLanguage(language)

        coVerify { appConfigurationDataSource.setAppLanguage(language) }
    }

    @Test
    fun `isFirstTimeUser should returns true when data source returns true`() = runTest {
        coEvery { appConfigurationDataSource.isFirstTimeLaunchApp() } returns true

        val result = repository.isFirstTimeUser()

        assertThat(result).isTrue()
    }

    @Test
    fun `setFirstTimeUser should set first time status to true when firstTime is true`() = runTest {
        val firstTime = true
        coEvery { appConfigurationDataSource.setFirstTimeStatus() } returns Unit

        repository.setFirstTimeUser(firstTime)

        coVerify { appConfigurationDataSource.setFirstTimeStatus() }
    }
}