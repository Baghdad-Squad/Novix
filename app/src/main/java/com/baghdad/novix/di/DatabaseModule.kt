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
    fun provideMovieDao(db: NovixDatabase): MovieDao = db.movieDao()

    @Provides
    fun provideTvShowDao(db: NovixDatabase): TvShowDao = db.tvShowDao()

    @Provides
    fun provideRecentlyViewedDao(db: NovixDatabase): RecentlyViewedDao = db.recentViewedDao()

    @Provides
    fun provideRecentSearchDao(db: NovixDatabase): RecentSearchDao = db.recentSearchDao()

    @Provides
    fun provideActorDao(db: NovixDatabase): ActorDao = db.actorDao()

    @Provides
    fun provideGenreDao(db: NovixDatabase): GenreDao = db.genreDao()

    @Provides
    fun provideFavoriteGenreDao(db: NovixDatabase): FavoriteGenreDao = db.favoriteGenreDao()

    @Provides
    fun provideSearchQueryDao(db: NovixDatabase): SearchQueryDao = db.searchQueryDao()

    @Provides
    fun provideContinueWatchingDao(db: NovixDatabase): ContinueWatchingDao = db.continueWatchingDao()
}
