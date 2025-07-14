package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.dto.LocalRecentlyViewedDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Upsert()
    suspend fun upsertRecentlyViewed(localRecentlyViewedDto: LocalRecentlyViewedDto)

    @Query("DELETE FROM RecentlyViewed")
    suspend fun clearAllRecentlyViewed()

    @Query("SELECT * FROM RecentlyViewed")
    fun getAllRecentlyViewed(): Flow<List<LocalRecentlyViewedDto>>

}
