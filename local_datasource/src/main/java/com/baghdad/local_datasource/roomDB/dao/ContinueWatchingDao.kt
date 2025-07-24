package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.ContinueWatching

@Dao
interface ContinueWatchingDao {

    @Upsert()
    suspend fun upsertContinueWatching(continueWatching: ContinueWatching)

    @Query("SELECT * FROM ContinueWatching WHERE userId = :userId LIMIT :pageSize OFFSET :offset")
    suspend fun getContinueWatching(userId: Long , pageSize: Int , offset: Int): List<ContinueWatching>

}