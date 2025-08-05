package com.baghdad.novix.di

import com.baghdad.local_datasource.LocalActorDataSourceImpl
import com.baghdad.local_datasource.LocalContinueWatchingDataSourceImpl
import com.baghdad.local_datasource.LocalFavoriteGenreDataSourceImpl
import com.baghdad.local_datasource.LocalGenreDataSourceImpl
import com.baghdad.local_datasource.LocalMovieDataSourceImpl
import com.baghdad.local_datasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.local_datasource.LocalSearchDataSourceImpl
import com.baghdad.local_datasource.LocalSearchQueryDataSourceImpl
import com.baghdad.local_datasource.LocalAppearanceDataSourceImpl
import com.baghdad.local_datasource.LocalTvShowDataSourceImpl
import com.baghdad.local_datasource.dataStore.session.LocalSessionDataStoreImpl
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalAppearanceDataSource
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import com.baghdad.repository.datasource.local.LocalUserDataStore
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
    abstract fun provideLocalActorDataSource(localActorDataSourceImpl: LocalActorDataSourceImpl): LocalActorDataSource

    @Binds
    abstract fun provideLocalContinueWatchingDataSource(localContinueWatchingDataSourceImpl: LocalContinueWatchingDataSourceImpl): LocalContinueWatchingDataSource

    @Binds
    abstract fun provideLocalFavoriteGenreDataSource(localFavoriteGenreDataSourceImpl: LocalFavoriteGenreDataSourceImpl): LocalFavoriteGenreDataSource

    @Binds
    abstract fun provideLocalGenreDataSource(localGenreDataSourceImpl: LocalGenreDataSourceImpl): LocalGenreDataSource

    @Binds
    abstract fun provideLocalMovieDataSource(localMovieDataSourceImpl: LocalMovieDataSourceImpl): LocalMovieDataSource

    @Binds
    abstract fun provideLocalRecentlyViewedDataSource(localRecentlyViewedDataSourceImpl: LocalRecentlyViewedDataSourceImpl): LocalRecentlyViewedDataSource

    @Binds
    abstract fun provideLocalSearchDataSource(localSearchDataSourceImpl: LocalSearchDataSourceImpl): LocalRecentSearchDataSource

    @Binds
    abstract fun provideLocalSearchQueryDataSource(localSearchQueryDataSourceImpl: LocalSearchQueryDataSourceImpl): LocalSearchQueryDataSource

    @Binds
    abstract fun provideLocalTvShowDataSource(localTvShowDataSourceImpl: LocalTvShowDataSourceImpl): LocalTvShowDataSource

    @Binds
    abstract fun provideLocalAppearanceDataSource(localAppearanceDataSourceImpl: LocalAppearanceDataSourceImpl): LocalAppearanceDataSource

}


