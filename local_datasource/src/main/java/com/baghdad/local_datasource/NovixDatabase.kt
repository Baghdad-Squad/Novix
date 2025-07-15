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
import com.baghdad.local_datasource.database.dto.LocalActorDto
import com.baghdad.local_datasource.database.dto.LocalGenreDto
import com.baghdad.local_datasource.database.dto.LocalMovieDto
import com.baghdad.local_datasource.database.dto.LocalRecentSearchDto
import com.baghdad.local_datasource.database.dto.LocalRecentlyViewedDto
import com.baghdad.local_datasource.database.dto.LocalTvShowDto

@Database(
    entities = [
        LocalTvShowDto::class,
        LocalMovieDto::class,
        LocalRecentSearchDto::class,
        LocalRecentlyViewedDto::class,
        LocalActorDto::class,
        LocalGenreDto::class
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