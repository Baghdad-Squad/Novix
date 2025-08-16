package com.baghdad.localDatasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.localDatasource.roomDB.entity.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Upsert
    fun upsertRecentSearch(recentSearch: RecentSearch)

    @Query("SELECT * FROM recent_search WHERE `query` = :query LIMIT 1")
    suspend fun getRecentSearchByQuery(query: String): RecentSearch?

    @Query("SELECT * FROM recent_search")
    fun getAllRecentSearch(): Flow<List<RecentSearch>>

    @Query("DELETE FROM recent_search")
    suspend fun clearAllRecentSearch()

    @Query("DELETE FROM recent_search WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)
}