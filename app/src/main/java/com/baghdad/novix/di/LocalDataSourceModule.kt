package com.baghdad.novix.di

import com.baghdad.local_datasource.LocalActorDataSourceImpl
import com.baghdad.local_datasource.LocalContinueWatchingDataSourceImpl
import com.baghdad.local_datasource.LocalFavoriteGenreDataSourceImpl
import com.baghdad.local_datasource.LocalGenreDataSourceImpl
import com.baghdad.local_datasource.LocalMovieDataSourceImpl
import com.baghdad.local_datasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.local_datasource.LocalSearchDataSourceImpl
import com.baghdad.local_datasource.LocalSearchQueryDataSourceImpl
import com.baghdad.local_datasource.LocalTvShowDataSourceImpl
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.datasource.local.LocalGenreDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalSearchQueryDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import dagger.Binds
import dagger.Module

@Module
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindLocalMovieDataSource(impl: LocalMovieDataSourceImpl): LocalMovieDataSource

    @Binds
    abstract fun bindLocalGenreDataSource(impl: LocalGenreDataSourceImpl): LocalGenreDataSource

    @Binds
    abstract fun bindLocalRecentSearchDataSource(impl: LocalSearchDataSourceImpl): LocalRecentSearchDataSource

    @Binds
    abstract fun bindLocalRecentlyViewedDataSource(impl: LocalRecentlyViewedDataSourceImpl): LocalRecentlyViewedDataSource

    @Binds
    abstract fun bindLocalTvShowDataSource(impl: LocalTvShowDataSourceImpl): LocalTvShowDataSource

    @Binds
    abstract fun bindLocalActorDataSource(impl: LocalActorDataSourceImpl): LocalActorDataSource

    @Binds
    abstract fun bindLocalFavoriteGenreDataSource(impl: LocalFavoriteGenreDataSourceImpl): LocalFavoriteGenreDataSource

    @Binds
    abstract fun bindLocalSearchQueryDataSource(impl: LocalSearchQueryDataSourceImpl): LocalSearchQueryDataSource

    @Binds
    abstract fun bindLocalContinueWatchingDataSource(impl: LocalContinueWatchingDataSourceImpl): LocalContinueWatchingDataSource
}