package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TrendingTvShow

@Dao
interface TrendingTvShowDao {
    @Upsert
    suspend fun upsertTvShow(tvShows: List<TrendingTvShow>)

    @Query(
        """ SELECT * FROM trending_tv_shows ORDER BY addedAt ASC LIMIT :pageSize OFFSET :offset"""
    )
    suspend fun getTrendingTvShows(
        pageSize: Int,
        offset: Int,
    ): List<TrendingTvShow>
}