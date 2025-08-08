package com.baghdad.local_datasource.roomDB.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.dao.SavedListMovieDao
import com.baghdad.local_datasource.roomDB.entity.ContinueWatching
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.SavedListMovie

@Database(
    entities = [
        RecentSearch::class,
        RecentlyViewed::class,
        ContinueWatching::class,
        SavedListMovie::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)

abstract class NovixDatabase : RoomDatabase(){

    abstract fun recentViewedDao(): RecentlyViewedDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun continueWatchingDao(): ContinueWatchingDao

    abstract fun savedListMovieDao(): SavedListMovieDao
}