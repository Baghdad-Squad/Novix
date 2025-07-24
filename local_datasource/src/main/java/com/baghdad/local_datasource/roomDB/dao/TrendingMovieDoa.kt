package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TrendingMovie

@Dao
interface TrendingMovieDoa {
    @Upsert
    suspend fun upsertMovies(movies: List<TrendingMovie>)

    @Query(
        """ SELECT * from TrendingMovie LIMIT :pageSize OFFSET :offset"""
    )
    suspend fun getTrendingMovie(
        pageSize: Int,
        offset: Int,
    ): List<TrendingMovie>
}