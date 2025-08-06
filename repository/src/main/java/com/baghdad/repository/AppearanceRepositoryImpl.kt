package com.baghdad.repository

import com.baghdad.domain.repository.AppearanceRepository
import com.baghdad.repository.datasource.local.LocalAppearanceDataSource
import javax.inject.Inject
import javax.inject.Singleton
import com.baghdad.domain.model.AppAppearanceMode as DomainAppThemeMode
import com.baghdad.repository.model.AppAppearanceMode as RepositoryAppThemeMode

@Singleton
class AppearanceRepositoryImpl@Inject constructor(
    private val localDataSource: LocalAppearanceDataSource
) : AppearanceRepository {

    override suspend fun setAppearanceMode(appearance: DomainAppThemeMode) {
        val repositoryTheme = when (appearance) {
            DomainAppThemeMode.LIGHT -> RepositoryAppThemeMode.LIGHT
            DomainAppThemeMode.DARK -> RepositoryAppThemeMode.DARK
        }
        localDataSource.saveAppearanceMode(repositoryTheme)
    }

    override suspend fun getAppearanceMode(): DomainAppThemeMode {
        val repositoryTheme = localDataSource.getAppearanceMode()
        return when (repositoryTheme) {
            RepositoryAppThemeMode.LIGHT -> DomainAppThemeMode.LIGHT
            RepositoryAppThemeMode.DARK -> DomainAppThemeMode.DARK
        }
    }
}