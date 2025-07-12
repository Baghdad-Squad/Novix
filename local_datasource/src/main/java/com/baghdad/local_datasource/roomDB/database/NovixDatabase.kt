package com.baghdad.local_datasource.roomDB.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentViewedDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.RecentViewed
import com.baghdad.local_datasource.roomDB.entity.TvShow

@Database(
    entities = [
        TvShow::class,
        Movie::class,
        RecentSearch::class,
        RecentViewed::class,
        Actor::class,
        Genre::class
    ],
    version = 1
)
@TypeConverters(Converters::class)

abstract class NovixDatabase : RoomDatabase(){

    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun actorDao(): ActorDao
    abstract fun genreDao(): GenreDao
}