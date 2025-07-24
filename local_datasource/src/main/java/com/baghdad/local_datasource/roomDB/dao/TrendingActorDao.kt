package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TrendingActorEntity

@Dao
interface TrendingActorDao {
    @Upsert
    suspend fun upsertTrendingActors(people: List<TrendingActorEntity>)

    @Query(
        """
    SELECT * FROM trendingActor
    LIMIT :pageSize OFFSET :offset
    """
    )
    suspend fun getAllTrendingActors(
        pageSize: Int,
        offset: Int
    ): List<TrendingActorEntity>


    @Query("SELECT * FROM trendingActor WHERE id = :personId")
    suspend fun getTrendingActorById(personId: Long): TrendingActorEntity

    @Query("DELETE FROM trendingActor")
    suspend fun deleteAllTrendingActor()

}