package com.baghdad.local_datasource.roomDB.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.local_datasource.roomDB.dao.ActorDao
import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.MovieDao
import com.baghdad.local_datasource.roomDB.dao.RecentSearchDao
import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.dao.SearchQueryDao
import com.baghdad.local_datasource.roomDB.dao.TrendingTvShowDao
import com.baghdad.local_datasource.roomDB.dao.TopRatedDao
import com.baghdad.local_datasource.roomDB.dao.TrendingActorDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.ContinueWatching
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.LocalFavoriteGenreDto
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import com.baghdad.local_datasource.roomDB.entity.TrendingTvShow
import com.baghdad.local_datasource.roomDB.entity.TopRatedMovie
import com.baghdad.local_datasource.roomDB.entity.TrendingActorEntity
import com.baghdad.local_datasource.roomDB.entity.TvShow

@Database(
    entities = [
        TvShow::class,
        Movie::class,
        RecentSearch::class,
        RecentlyViewed::class,
        Actor::class,
        Genre::class,
        LocalFavoriteGenreDto::class,
        SearchQuery::class,
        TrendingTvShow::class
        SearchQuery::class,
        ContinueWatching::class,
        TopRatedMovie::class,
        TrendingActorEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)

abstract class NovixDatabase : RoomDatabase(){

    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun recentViewedDao(): RecentlyViewedDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun actorDao(): ActorDao
    abstract fun genreDao(): GenreDao
    abstract fun favoriteGenreDao(): FavoriteGenreDao
    abstract fun searchQueryDao(): SearchQueryDao
    abstract fun trendingTvShowDao(): TrendingTvShowDao
    abstract fun topRatedDao(): TopRatedDao
    abstract fun trendingActorDao(): TrendingActorDao
    abstract fun continueWatchingDao(): ContinueWatchingDao
}