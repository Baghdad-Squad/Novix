package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.entity.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Upsert
    fun addRecentSearch(recentSearch: RecentSearch)

    @Query("SELECT * FROM RecentlySearch")
    fun getAllRecentSearch(): Flow<List<RecentSearch>>

    @Query("SELECT * FROM RecentlySearch WHERE id = :id")
    suspend fun getRecentSearchById(id: Long): List<RecentSearch>

    @Query("SELECT * FROM RecentlySearch ORDER BY searchedAt DESC")
    fun getLastTenRecentSearchItems(): Flow<List<RecentSearch>>


    @Query("DELETE FROM RecentlySearch")
    suspend fun clearAllRecentSearch()

    @Query("SELECT COUNT(*) FROM RecentlySearch")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM RecentlySearch WHERE id = :id)")
    suspend fun exists(id: Long): Boolean

    @Query("DELETE FROM RecentlySearch WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)
}