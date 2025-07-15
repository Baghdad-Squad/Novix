package com.baghdad.novix.di

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.repository.MovieRepositoryImpl
import com.baghdad.repository.RecentlyViewedRepositoryImpl
import com.baghdad.repository.SearchRepositoryImpl
import com.baghdad.repository.TvShowRepositoryImpl
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(
            searchRemoteDataSource = get<RemoteSearchDataSource>(),
            remoteGenreDataSource = get(),
            localRecentSearchDataSource = get(),
            localActorDataSource = get(),
            localMovieDataSource = get(),
            localTvShowDataSource = get(),
            localGenreDataSource = get()
        )
    }

    single<MovieRepository> {
        MovieRepositoryImpl(
            remoteGenreDataSource = get()
        )
    }
    single<TvShowRepository> {
        TvShowRepositoryImpl(
            remoteGenreDataSource = get()
        )
    }

    single<RecentlyViewedRepository> {
        RecentlyViewedRepositoryImpl(
            localRecentlyViewedDataSource = get(),
            localFavoriteGenreDataSource = get(),
            localMovieDataSource = get(),
            localTvShowDataSource = get(),
        )
    }

}