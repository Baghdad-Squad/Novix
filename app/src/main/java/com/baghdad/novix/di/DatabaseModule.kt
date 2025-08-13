package com.baghdad.novix.di

import android.content.Context
import androidx.room.Room
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.localDatasource.roomDB.dao.SavedListMovieDao
import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.localDatasource.roomDB.database.NovixDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    const val APP_DATABASE_NAME = "Novix"

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NovixDatabase {
        return Room.databaseBuilder(
            context,
            NovixDatabase::class.java,
            APP_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideRecentlyViewedDao(database: NovixDatabase): RecentlyViewedDao {
        return database.recentViewedDao()
    }

    @Provides
    fun provideSavedListMovieDao(database: NovixDatabase): SavedListMovieDao {
        return database.savedListMovieDao()
    }

    @Provides
    fun provideRecentSearchDao(database: NovixDatabase): RecentSearchDao {
        return database.recentSearchDao()
    }

    @Provides
    fun provideContinueWatchingDao(database: NovixDatabase): UserWatchedMediaDao {
        return database.continueWatchingDao()
    }
}
