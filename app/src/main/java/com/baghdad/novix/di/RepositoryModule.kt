package com.baghdad.novix.di

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.repository.ActorRepositoryImpl
import com.baghdad.repository.AuthenticationRepositoryImpl
import com.baghdad.repository.ContinueWatchingRepositoryImpl
import com.baghdad.repository.EpisodeRepositoryImpl
import com.baghdad.repository.FavoriteGenreRepositoryImpl
import com.baghdad.repository.MovieRepositoryImpl
import com.baghdad.repository.RecentlyViewedRepositoryImpl
import com.baghdad.repository.SearchRepositoryImpl
import com.baghdad.repository.TvShowRepositoryImpl
import com.baghdad.repository.datasource.local.LocalActorDataSource
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
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideSearchRepository(
        searchRemoteDataSource: RemoteSearchDataSource,
        remoteGenreDataSource: RemoteGenreDataSource,
        localRecentSearchDataSource: LocalRecentSearchDataSource,
        localActorDataSource: LocalActorDataSource,
        localMovieDataSource: LocalMovieDataSource,
        localTvShowDataSource: LocalTvShowDataSource,
        localGenreDataSource: LocalGenreDataSource,
        localSearchQueryDataSource: LocalSearchQueryDataSource,
    ): SearchRepository {
        return SearchRepositoryImpl(
            searchRemoteDataSource = searchRemoteDataSource,
            remoteGenreDataSource = remoteGenreDataSource,
            localRecentSearchDataSource = localRecentSearchDataSource,
            localActorDataSource = localActorDataSource,
            localMovieDataSource = localMovieDataSource,
            localTvShowDataSource = localTvShowDataSource,
            localGenreDataSource = localGenreDataSource,
            localSearchQueryDataSource = localSearchQueryDataSource
        )
    }

    @Provides
    fun provideMovieRepository(
        remoteGenreDataSource: RemoteGenreDataSource,
        localGenreDataSource: LocalGenreDataSource,
        remoteMovieDataSource: RemoteMovieDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            localGenreDataSource = localGenreDataSource,
            remoteMovieDataSource = remoteMovieDataSource
        )
    }

    @Provides
    fun provideTvShowRepository(
        remoteGenreDataSource: RemoteGenreDataSource,
        tvShowRemoteDataSource: RemoteTvShowDataSource,
    ): TvShowRepository {
        return TvShowRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            tvShowRemoteDataSource = tvShowRemoteDataSource
        )
    }

    @Provides
    fun provideRecentlyViewedRepository(
        localRecentlyViewedDataSource: LocalRecentlyViewedDataSource,
        localFavoriteGenreDataSource: LocalFavoriteGenreDataSource,
        localMovieDataSource: LocalMovieDataSource,
        localTvShowDataSource: LocalTvShowDataSource,
    ): RecentlyViewedRepository {
        return RecentlyViewedRepositoryImpl(
            localRecentlyViewedDataSource = localRecentlyViewedDataSource,
            localFavoriteGenreDataSource = localFavoriteGenreDataSource,
            localMovieDataSource = localMovieDataSource,
            localTvShowDataSource = localTvShowDataSource
        )
    }

    @Provides
    fun provideActorRepository(
        remoteActorDataSource: RemoteActorDataSource,
    ): ActorRepository {
        return ActorRepositoryImpl(
            remoteActorDataSource = remoteActorDataSource
        )
    }

    @Provides
    fun provideEpisodeRepository(
        remoteEpisodeDataSource: RemoteEpisodeDataSource,
        remoteTvShowDataSource: RemoteTvShowDataSource,
    ): EpisodeRepository {
        return EpisodeRepositoryImpl(
            remoteEpisodeDataSource = remoteEpisodeDataSource,
            remoteTvShowDataSource = remoteTvShowDataSource
        )
    }

    @Provides
    fun provideFavoriteGenreRepository(
        favoriteGenreDataSource: LocalFavoriteGenreDataSource,
    ): FavoriteGenreRepository {
        return FavoriteGenreRepositoryImpl(
            favoriteGenreDataSource = favoriteGenreDataSource
        )

    }

    @Provides
    fun provideContinueWatchingRepository(
        localContinueWatchingDataSource: LocalContinueWatchingDataSource,
    ): ContinueWatchingRepository {
        return ContinueWatchingRepositoryImpl(
            localContinueWatchingDataSource = localContinueWatchingDataSource
        )
    }

    @Provides
    fun provideAuthenticationRepository(
        remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
        localSessionDataStore: LocalSessionDataStore,
        localUserDataStore: LocalUserDataStore,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            remoteAuthenticationDataSource = remoteAuthenticationDataSource,
            localSessionDataStore = localSessionDataStore,
            localUserDataStore = localUserDataStore
        )
    }
}