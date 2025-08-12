package com.baghdad.novix.di

import com.baghdad.localDatasource.ContinueWatchingDataSourceImpl
import com.baghdad.localDatasource.RecentSearchDataSourceImpl
import com.baghdad.localDatasource.RecentlyViewedDataSourceImpl
import com.baghdad.localDatasource.SavableMovieDataSourceImpl
import com.baghdad.localDatasource.SessionDataSourceImpl
import com.baghdad.localDatasource.UserDataSourceImpl
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.datasource.local.ContinueWatchingDataSource
import com.baghdad.repository.datasource.local.RecentSearchDataSource
import com.baghdad.repository.datasource.local.RecentlyViewedDataSource
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.SessionDataSource
import com.baghdad.repository.datasource.local.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun provideLocalSessionDataStore(localSessionDataStoreImpl: SessionDataSourceImpl): SessionDataSource

    @Binds
    abstract fun provideLocalUserDataStore(localUserDataStoreImpl: UserDataSourceImpl): UserDataSource

    @Binds
    abstract fun provideLocalRecentSearchDataSource(localRecentSearchDataSourceImpl: RecentSearchDataSourceImpl): RecentSearchDataSource

    @Binds
    abstract fun provideLocalContinueWatchingDataSource(localContinueWatchingDataSourceImpl: ContinueWatchingDataSourceImpl): ContinueWatchingDataSource

    @Binds
    abstract fun provideLocalRecentlyViewedDataSource(localRecentlyViewedDataSourceImpl: RecentlyViewedDataSourceImpl): RecentlyViewedDataSource

    @Binds
    abstract fun provideLocalUserPreferencesDataSource(localUserPreferencesDataSourceImpl: com.baghdad.localDatasource.AppConfigurationDataSourceImpl): AppConfigurationDataSource

    @Binds
    abstract fun provideLocalSavableMovieDataSource(
        localSavableMovieDataSourceImpl: SavableMovieDataSourceImpl,
    ): SavableMovieDataSource
}


