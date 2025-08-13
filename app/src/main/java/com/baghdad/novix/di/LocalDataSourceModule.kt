package com.baghdad.novix.di

import com.baghdad.localDatasource.AppConfigurationDataSourceImpl
import com.baghdad.localDatasource.RecentSearchDataSourceImpl
import com.baghdad.localDatasource.RecentlyViewedDataSourceImpl
import com.baghdad.localDatasource.SavableMovieDataSourceImpl
import com.baghdad.localDatasource.SessionDataSourceImpl
import com.baghdad.localDatasource.UserDataSourceImpl
import com.baghdad.localDatasource.UserWatchedMediaDataSourceImpl
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.datasource.local.RecentSearchDataSource
import com.baghdad.repository.datasource.local.RecentlyViewedDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.SessionDataSource
import com.baghdad.repository.datasource.local.UserDataSource
import com.baghdad.repository.datasource.local.UserWatchedMediaDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun provideSessionDataSource(sessionDataSourceImpl: SessionDataSourceImpl): SessionDataSource

    @Binds
    abstract fun provideUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    abstract fun provideRecentSearchDataSource(recentSearchDataSourceImpl: RecentSearchDataSourceImpl): RecentSearchDataSource

    @Binds
    abstract fun provideLocalContinueWatchingDataSource(localContinueWatchingDataSourceImpl: UserWatchedMediaDataSourceImpl): UserWatchedMediaDataSource

    @Binds
    abstract fun provideRecentlyViewedDataSource(recentlyViewedDataSourceImpl: RecentlyViewedDataSourceImpl): RecentlyViewedDataSource

    @Binds
    abstract fun provideUserPreferencesDataSource(userPreferencesDataSourceImpl: AppConfigurationDataSourceImpl): AppConfigurationDataSource

    @Binds
    abstract fun provideSavableMovieDataSource(
        savableMovieDataSourceImpl: SavableMovieDataSourceImpl,
    ): SavableMovieDataSource
}


