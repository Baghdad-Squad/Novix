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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
    singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }
    singleOf(::TvShowRepositoryImpl) { bind<TvShowRepository>() }
    singleOf(::RecentlyViewedRepositoryImpl) { bind<RecentlyViewedRepository>() }
    singleOf(::ActorRepositoryImpl) { bind<ActorRepository>() }
    singleOf(::EpisodeRepositoryImpl) { bind<EpisodeRepository>() }
    singleOf(::FavoriteGenreRepositoryImpl) { bind<FavoriteGenreRepository>() }
    singleOf(::ContinueWatchingRepositoryImpl) { bind<ContinueWatchingRepository>() }
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
}
