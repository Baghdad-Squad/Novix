package com.baghdad.novix.di

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.repository.SavedListRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.repository.ActorRepositoryImpl
import com.baghdad.repository.AuthenticationRepositoryImpl
import com.baghdad.repository.ContinueWatchingRepositoryImpl
import com.baghdad.repository.EpisodeRepositoryImpl
import com.baghdad.repository.FavoriteGenreRepositoryImpl
import com.baghdad.repository.MovieRepositoryImpl
import com.baghdad.repository.RecentlyViewedRepositoryImpl
import com.baghdad.repository.SavedListRepositoryImpl
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
    abstract fun provideSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun provideTvShowRepository(tvShowRepositoryImpl: TvShowRepositoryImpl): TvShowRepository

    @Binds
    abstract fun provideRecentlyViewedRepository(recentlyViewedRepositoryImpl: RecentlyViewedRepositoryImpl): RecentlyViewedRepository

    @Binds
    abstract fun provideActorRepository(actorRepositoryImpl: ActorRepositoryImpl): ActorRepository

    @Binds
    abstract fun provideEpisodeRepository(episodeRepositoryImpl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    abstract fun provideFavoriteGenreRepository(favoriteGenreRepositoryImpl: FavoriteGenreRepositoryImpl): FavoriteGenreRepository

    @Binds
    abstract fun provideContinueWatchingRepository(continueWatchingRepositoryImpl: ContinueWatchingRepositoryImpl): ContinueWatchingRepository

    @Binds
    abstract fun provideAuthRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    abstract fun provideSavedListRepository(savedListRepositoryImpl: SavedListRepositoryImpl): SavedListRepository

}