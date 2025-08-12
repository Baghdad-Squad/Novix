package com.baghdad.novix.di

import com.baghdad.localDatasource.LocalRecentSearchDataSourceImpl
import com.baghdad.localDatasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.localDatasource.LocalSavableMovieDataSourceImpl
import com.baghdad.localDatasource.LocalSessionDataSourceImpl
import com.baghdad.localDatasource.LocalUserDataSourceImpl
import com.baghdad.localDatasource.LocalUserWatchedMediaDataSourceImpl
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalSavableMovieDataSource
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.local.LocalUserDataSource
import com.baghdad.repository.datasource.local.LocalUserWatchedMediaDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun provideLocalSessionDataStore(localSessionDataStoreImpl: LocalSessionDataSourceImpl): LocalSessionDataSource

    @Binds
    abstract fun provideLocalUserDataStore(localUserDataStoreImpl: LocalUserDataSourceImpl): LocalUserDataSource

    @Binds
    abstract fun provideLocalRecentSearchDataSource(localRecentSearchDataSourceImpl: LocalRecentSearchDataSourceImpl): LocalRecentSearchDataSource

    @Binds
    abstract fun provideLocalContinueWatchingDataSource(localContinueWatchingDataSourceImpl: LocalUserWatchedMediaDataSourceImpl): LocalUserWatchedMediaDataSource

    @Binds
    abstract fun provideLocalRecentlyViewedDataSource(localRecentlyViewedDataSourceImpl: LocalRecentlyViewedDataSourceImpl): LocalRecentlyViewedDataSource

    @Binds
    abstract fun provideLocalUserPreferencesDataSource(localUserPreferencesDataSourceImpl: com.baghdad.localDatasource.AppConfigurationDataSourceImpl): AppConfigurationDataSource

    @Binds
    abstract fun provideLocalSavableMovieDataSource(
        localSavableMovieDataSourceImpl: LocalSavableMovieDataSourceImpl,
    ): LocalSavableMovieDataSource
}


