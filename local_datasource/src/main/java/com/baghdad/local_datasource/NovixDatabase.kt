package com.baghdad.local_datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baghdad.local_datasource.database.dao.ActorDao
import com.baghdad.local_datasource.database.dao.GenreDao
import com.baghdad.local_datasource.database.dao.MovieDao
import com.baghdad.local_datasource.database.dao.RecentSearchDao
import com.baghdad.local_datasource.database.dao.RecentlyViewedDao
import com.baghdad.local_datasource.database.dao.TvShowDao
import com.baghdad.local_datasource.database.entity.Actor
import com.baghdad.local_datasource.database.entity.Genre
import com.baghdad.local_datasource.database.entity.Movie
import com.baghdad.local_datasource.database.entity.RecentSearch
import com.baghdad.local_datasource.database.entity.RecentlyViewed
import com.baghdad.local_datasource.database.entity.TvShow

@Database(
    entities = [
        TvShow::class,
        Movie::class,
        RecentSearch::class,
        RecentlyViewed::class,
        Actor::class,
        Genre::class
    ],
    version = 1
)
@TypeConverters(Converters::class)

abstract class NovixDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun recentViewedDao(): RecentlyViewedDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun actorDao(): ActorDao
    abstract fun genreDao(): GenreDao
}