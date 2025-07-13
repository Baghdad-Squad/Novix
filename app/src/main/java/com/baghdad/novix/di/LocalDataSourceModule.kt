package com.baghdad.novix.di

import androidx.room.Room
import com.baghdad.local_datasource.LocalActorDataSourceImpl
import com.baghdad.local_datasource.LocalGenreDataSource
import com.baghdad.local_datasource.LocalMovieDataSourceImpl
import com.baghdad.local_datasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.local_datasource.LocalSearchDataSourceImpl
import com.baghdad.local_datasource.LocalTvShowDataSourceImpl
import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.repository.datasource.local.LocalActorDataSource
import com.baghdad.repository.datasource.local.LocalMovieDataSource
import com.baghdad.repository.datasource.local.LocalRecentSearchDataSource
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.datasource.local.LocalTvShowDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {
    single<NovixDatabase> {
        Room.databaseBuilder(
            androidContext(),
            NovixDatabase::class.java,
            "Novix"
        ).build()
    }

    single<MovieDao> { get<NovixDatabase>().movieDao() }
    single<TvShowDao> { get<NovixDatabase>().tvShowDao() }
    single<RecentlyViewedDao> { get<NovixDatabase>().recentViewedDao() }
    single<RecentSearchDao> { get<NovixDatabase>().recentSearchDao() }
    single<ActorDao> { get<NovixDatabase>().actorDao() }
    single<GenreDao> { get<NovixDatabase>().genreDao() }
    single<LocalMovieDataSource> {
        LocalMovieDataSourceImpl(
            movieDao = get<MovieDao>()
        )
    }


    single<LocalGenreDataSource> {
        LocalGenreDataSource(
            genreDao = get<GenreDao>()
        )
    }
    single<LocalRecentSearchDataSource> {
        LocalSearchDataSourceImpl(
            recentSearchDao = get<RecentSearchDao>()
        )
    }

    single<LocalRecentlyViewedDataSource> {
        LocalRecentlyViewedDataSourceImpl(
            recentlyViewedDao = get<RecentlyViewedDao>(),
            movieDao = get<MovieDao>(),
            tvShowDao = get<TvShowDao>()
        )
    }

    single<LocalTvShowDataSource> {
        LocalTvShowDataSourceImpl(
            tvShowDao = get<TvShowDao>()
        )
    }

    single<LocalActorDataSource> {
        LocalActorDataSourceImpl(
            actorDao = get<ActorDao>()
        )
    }

}