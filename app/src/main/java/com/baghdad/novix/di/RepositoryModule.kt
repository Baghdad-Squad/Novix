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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun provideMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun provideTvShowRepository(impl: TvShowRepositoryImpl): TvShowRepository

    @Binds
    abstract fun provideRecentlyViewedRepository(impl: RecentlyViewedRepositoryImpl): RecentlyViewedRepository

    @Binds
    abstract fun provideActorRepository(impl: ActorRepositoryImpl): ActorRepository

    @Binds
    abstract fun provideEpisodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    abstract fun provideFavoriteGenreRepository(impl: FavoriteGenreRepositoryImpl): FavoriteGenreRepository

    @Binds
    abstract fun provideContinueWatchingRepository(impl: ContinueWatchingRepositoryImpl): ContinueWatchingRepository

    @Binds
    abstract fun provideAuthRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository


}
