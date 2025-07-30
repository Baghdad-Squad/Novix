package com.baghdad.novix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.baghdad.local_datasource.LocalActorDataSourceImpl
import com.baghdad.local_datasource.LocalContinueWatchingDataSourceImpl
import com.baghdad.local_datasource.LocalFavoriteGenreDataSourceImpl
import com.baghdad.local_datasource.LocalGenreDataSourceImpl
import com.baghdad.local_datasource.LocalMovieDataSourceImpl
import com.baghdad.local_datasource.LocalRecentlyViewedDataSourceImpl
import com.baghdad.local_datasource.LocalSearchDataSourceImpl
import com.baghdad.local_datasource.LocalSearchQueryDataSourceImpl
import com.baghdad.local_datasource.LocalTvShowDataSourceImpl
import com.baghdad.local_datasource.dataStore.session.LocalSessionDataStoreImpl
import com.baghdad.local_datasource.dataStore.user.LocalUserDataStoreImpl
import com.baghdad.local_datasource.dataStore.user.UserSerializer
import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.dao.SearchQueryDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
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
import com.example.application.proto.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = "app-preferences"
)
val Context.userDataStore: DataStore<User> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)

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
    single<FavoriteGenreDao> { get<NovixDatabase>().favoriteGenreDao() }
    single<SearchQueryDao> { get<NovixDatabase>().searchQueryDao() }
    single<ContinueWatchingDao> { get<NovixDatabase>().continueWatchingDao() }



    singleOf(::LocalMovieDataSourceImpl) { bind<LocalMovieDataSource>() }
    singleOf(::LocalGenreDataSourceImpl) { bind<LocalGenreDataSource>() }
    singleOf(::LocalSearchDataSourceImpl) { bind<LocalRecentSearchDataSource>() }
    singleOf(::LocalRecentlyViewedDataSourceImpl) { bind<LocalRecentlyViewedDataSource>() }
    singleOf(::LocalTvShowDataSourceImpl) { bind<LocalTvShowDataSource>() }
    singleOf(::LocalActorDataSourceImpl) { bind<LocalActorDataSource>() }
    singleOf(::LocalFavoriteGenreDataSourceImpl) { bind<LocalFavoriteGenreDataSource>() }
    singleOf(::LocalSearchQueryDataSourceImpl) { bind<LocalSearchQueryDataSource>() }
    singleOf(::LocalContinueWatchingDataSourceImpl){bind<LocalContinueWatchingDataSource>()}


    single<DataStore<Preferences>>(named("preferences")) {
        androidContext().datastore
    }
    single<DataStore<User>>(named("user")) {
        androidContext().userDataStore
    }
    single<LocalSessionDataStore> {
        LocalSessionDataStoreImpl(
            dataStore = get(named("preferences")),
            logger = get()
        )
    }
    single<LocalUserDataStore> {
        LocalUserDataStoreImpl(
            dataStore = get(named("user")),
            logger = get()
        )
    }
}
