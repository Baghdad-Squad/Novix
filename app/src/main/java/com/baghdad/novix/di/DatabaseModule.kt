package com.baghdad.novix.di

import android.content.Context
import androidx.room.Room
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NovixDatabase {
        return Room.databaseBuilder(
            context,
            NovixDatabase::class.java,
            "Novix"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: NovixDatabase): MovieDao = database.movieDao()

    @Provides
    fun provideTvShowDao(database: NovixDatabase): TvShowDao = database.tvShowDao()

    @Provides
    fun provideRecentlyViewedDao(database: NovixDatabase): RecentlyViewedDao = database.recentViewedDao()

    @Provides
    fun provideRecentSearchDao(database: NovixDatabase): RecentSearchDao = database.recentSearchDao()

    @Provides
    fun provideActorDao(database: NovixDatabase): ActorDao = database.actorDao()

    @Provides
    fun provideGenreDao(database: NovixDatabase): GenreDao = database.genreDao()

    @Provides
    fun provideFavoriteGenreDao(database: NovixDatabase): FavoriteGenreDao = database.favoriteGenreDao()

    @Provides
    fun provideSearchQueryDao(database: NovixDatabase): SearchQueryDao = database.searchQueryDao()

    @Provides
    fun provideContinueWatchingDao(database: NovixDatabase): ContinueWatchingDao = database.continueWatchingDao()
}
