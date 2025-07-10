package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM RecentSearch")
    suspend fun getAllRecentSearch(): Flow<List<RecentSearch>>

    @Query("SELECT * FROM RecentSearch WHERE id = :id")
    suspend fun getRecentSearchById(id: Long)

    @Transaction
    @Query("""
        SELECT * FROM RecentSearch
        ORDER BY time DESC 
        LIMIT 10
    """)
    fun getLastTenRecentSearchItems(): Flow<List<RecentViewedWithDetails>>


    @Query("DElETE FROM RecentSearch")
    suspend fun clearAllRecentSearch()

    @Query("SELECT COUNT(*) FROM RecentSearch")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM RecentSearch WHERE id = :id)")
    suspend fun exists(id: Long): Boolean

    @Query("DELETE FROM RecentSearch WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)


}