package com.baghdad.localDatasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Upsert
    suspend fun upsertRecentlyViewed(recentlyViewed: RecentlyViewed)

    @Query("DELETE FROM recently_viewed")
    suspend fun deleteAllRecentlyViewed()

    @Query("SELECT * FROM recently_viewed")
    fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>>
}