package com.baghdad.localDatasource.roomDB.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baghdad.localDatasource.roomDB.converter.Converters
import com.baghdad.localDatasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.localDatasource.roomDB.dao.RecentSearchDao
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.localDatasource.roomDB.dao.SavedListMovieDao
import com.baghdad.localDatasource.roomDB.entity.ContinueWatching
import com.baghdad.localDatasource.roomDB.entity.RecentSearch
import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
import com.baghdad.localDatasource.roomDB.entity.SavedListMovie

@Database(
    entities = [
        RecentSearch::class,
        RecentlyViewed::class,
        ContinueWatching::class,
        SavedListMovie::class,
    ], version = 1
)

@TypeConverters(Converters::class)
abstract class NovixDatabase : RoomDatabase() {
    abstract fun recentViewedDao(): RecentlyViewedDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun continueWatchingDao(): ContinueWatchingDao
    abstract fun savedListMovieDao(): SavedListMovieDao
}