package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Upsert()
    suspend fun upsertRecentlyViewed(recentlyViewed: RecentlyViewed)


    @Query("DELETE FROM RecentlyViewed")
    suspend fun clearAllRecentlyViewed()


    @Query("SELECT * FROM RecentlyViewed")
    fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>>
}