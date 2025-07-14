package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.dto.LocalRecentSearchDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Upsert
    fun addRecentSearch(localRecentSearchDto: LocalRecentSearchDto)

    @Query("SELECT * FROM RecentlySearch")
    fun getAllRecentSearch(): Flow<List<LocalRecentSearchDto>>

    @Query("DELETE FROM RecentlySearch")
    suspend fun clearAllRecentSearch()

    @Query("DELETE FROM RecentlySearch WHERE id = :id")
    suspend fun deleteRecentSearchById(id: Long)
}