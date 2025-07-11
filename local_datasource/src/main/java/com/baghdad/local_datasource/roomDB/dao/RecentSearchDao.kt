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

    @Query("SELECT * FROM RecentSearch WHERE id = :id")
    suspend fun getRecentSearchById(id: Long): List<RecentSearch>

    @Query("SELECT * FROM RecentSearch ORDER BY searchedAt DESC")
    fun getLastTenRecentSearchItems(): Flow<List<RecentSearch>>


    @Query("DELETE FROM RecentSearch")
    suspend fun clearAllRecentSearch()

    @Query("SELECT COUNT(*) FROM RecentSearch")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM RecentSearch WHERE id = :id)")
    suspend fun exists(id: Long): Boolean

    @Query("DELETE FROM RecentSearch WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)
}