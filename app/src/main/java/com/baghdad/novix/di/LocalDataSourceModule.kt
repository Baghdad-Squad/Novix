package com.baghdad.novix.di


import com.baghdad.local_datasource.LocalContinueWatchingDataSourceImpl
import com.baghdad.local_datasource.LocalRecentSearchDataSourceImpl
import com.baghdad.local_datasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.local_datasource.dataStore.session.LocalOnboardingDatastoreImp
import com.baghdad.local_datasource.dataStore.session.LocalSessionDataStoreImpl
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalOnboardingDatastore
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.local.LocalUserPreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun provideLocalSessionDataStore(localSessionDataStoreImpl: LocalSessionDataStoreImpl): LocalSessionDataStore

    @Binds
    abstract fun provideLocalUserDataStore(localUserDataStoreImpl: LocalUserDataStoreImpl): LocalUserDataStore

    @Binds
    abstract fun provideLocalRecentSearchDataSource(localRecentSearchDataSourceImpl: LocalRecentSearchDataSourceImpl): LocalRecentSearchDataSource

    @Binds
    abstract fun provideLocalContinueWatchingDataSource(localContinueWatchingDataSourceImpl: LocalContinueWatchingDataSourceImpl): LocalContinueWatchingDataSource

    @Binds
    abstract fun provideLocalRecentlyViewedDataSource(localRecentlyViewedDataSourceImpl: LocalRecentlyViewedDataSourceImpl): LocalRecentlyViewedDataSource

    @Binds
    abstract fun provideLocalUserPreferencesDataSource(localUserPreferencesDataSourceImpl: com.baghdad.local_datasource.LocalUserPreferencesDataSourceImpl): LocalUserPreferencesDataSource

    @Binds
    abstract fun provideLocalOnBoardingDataStore(localOnboardingDatastore: LocalOnboardingDatastoreImp): LocalOnboardingDatastore
}


