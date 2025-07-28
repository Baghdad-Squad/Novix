package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Upsert
    fun addRecentSearch(recentSearch: RecentSearch)

    @Query("SELECT * FROM RecentSearch")
    fun getAllRecentSearch(): Flow<List<RecentSearch>>

    @Query("DELETE FROM RecentSearch")
    suspend fun clearAllRecentSearch()

    @Query("DELETE FROM RecentSearch WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)
}