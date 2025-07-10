package com.baghdad.local_datasource.roomDB.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentViewedDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.RecentViewed
import com.baghdad.local_datasource.roomDB.entity.TvShow

@Database(
    entities = [
        TvShow::class,
        Movie::class,
        RecentSearch::class,
        RecentViewed::class
    ],
    version = 1
)
abstract class NovixDatabase : RoomDatabase(){

    abstract val movieDao : MovieDao
    abstract val tvShowDao: TvShowDao
    abstract val recentViewedDao: RecentViewedDao
    abstract val recentSearchDao: RecentSearchDao
}