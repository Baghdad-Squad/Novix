package com.baghdad.novix.di

import com.baghdad.remoteDataSource.RemoteActorDataSourceImpl
import com.baghdad.remoteDataSource.RemoteAuthenticationImpl
import com.baghdad.remoteDataSource.RemoteEpisodeDataSourceImpl
import com.baghdad.remoteDataSource.RemoteGenreDataSourceImpl
import com.baghdad.remoteDataSource.RemoteMovieDataSourceImpl
import com.baghdad.remoteDataSource.RemoteSearchDataSourceImpl
import com.baghdad.remoteDataSource.RemoteTvShowDataSourceImpl
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteSearchDataSource(
        impl: RemoteSearchDataSourceImpl
    ): RemoteSearchDataSource

    @Binds
    abstract fun bindRemoteGenreDataSource(
        impl: RemoteGenreDataSourceImpl
    ): RemoteGenreDataSource

    @Binds
    abstract fun bindRemoteMovieDataSource(
        impl: RemoteMovieDataSourceImpl
    ): RemoteMovieDataSource

    @Binds
    abstract fun bindRemoteActorDataSource(
        impl: RemoteActorDataSourceImpl
    ): RemoteActorDataSource

    @Binds
    abstract fun bindRemoteTvShowDataSource(
        impl: RemoteTvShowDataSourceImpl
    ): RemoteTvShowDataSource

    @Binds
    abstract fun bindRemoteEpisodeDataSource(
        impl: RemoteEpisodeDataSourceImpl
    ): RemoteEpisodeDataSource

    @Binds
    abstract fun bindRemoteAuthenticationDataSource(
        impl: RemoteAuthenticationImpl
    ): RemoteAuthenticationDataSource
}