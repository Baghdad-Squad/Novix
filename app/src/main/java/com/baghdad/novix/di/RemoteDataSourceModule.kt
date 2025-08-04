package com.baghdad.novix.di

import com.baghdad.remoteDataSource.RemoteActorDataSourceImpl
import com.baghdad.remoteDataSource.RemoteAuthenticationImpl
import com.baghdad.remoteDataSource.RemoteEpisodeDataSourceImpl
import com.baghdad.remoteDataSource.RemoteGenreDataSourceImpl
import com.baghdad.remoteDataSource.RemoteMovieDataSourceImpl
import com.baghdad.remoteDataSource.RemoteSavedListDataSourceImpl
import com.baghdad.remoteDataSource.RemoteSearchDataSourceImpl
import com.baghdad.remoteDataSource.RemoteTvShowDataSourceImpl
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
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
    abstract fun provideRemoteAuthenticationImpl(remoteAuthenticationImpl: RemoteAuthenticationImpl): RemoteAuthenticationDataSource

    @Binds
    abstract fun provideSearchRemoteDataSource(remoteSearchDataSourceImpl: RemoteSearchDataSourceImpl): RemoteSearchDataSource

    @Binds
    abstract fun provideRemoteGenreDataSource(remoteGenreDataSourceImpl: RemoteGenreDataSourceImpl): RemoteGenreDataSource

    @Binds
    abstract fun provideRemoteActorDataSource(remoteActorDataSourceImpl: RemoteActorDataSourceImpl): RemoteActorDataSource


    @Binds
    abstract fun provideRemoteMovieDataSource(remoteMovieDataSourceImpl: RemoteMovieDataSourceImpl): RemoteMovieDataSource


    @Binds
    abstract fun provideRemoteEpisodeDataSource(remoteEpisodeDataSourceImpl: RemoteEpisodeDataSourceImpl): RemoteEpisodeDataSource

    @Binds
    abstract fun provideTvShowRemoteDataSource(remoteTvShowDataSourceImpl: RemoteTvShowDataSourceImpl): RemoteTvShowDataSource

    @Binds
    abstract fun provideRemoteSavedListDataSource(remoteSavedListDataSourceImpl: RemoteSavedListDataSourceImpl): RemoteSavedListDataSource

}
