package com.baghdad.novix.di

import android.content.Context
import androidx.room.Room
import com.baghdad.localDatasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.localDatasource.roomDB.dao.SavedListMovieDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
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
    fun provideRecentlyViewedDao(database: NovixDatabase): RecentlyViewedDao = database.recentViewedDao()

    @Provides
    fun provideSavedListMovieDao(database: NovixDatabase): SavedListMovieDao = database.savedListMovieDao()

    @Provides
    fun provideRecentSearchDao(database: NovixDatabase): RecentSearchDao = database.recentSearchDao()

    @Provides
    fun provideContinueWatchingDao(database: NovixDatabase): ContinueWatchingDao = database.continueWatchingDao()
}
