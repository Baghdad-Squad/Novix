package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.baghdad.local_datasource.roomDB.entity.SearchQuery

@Dao
interface SearchQueryDao {

    @Insert(onConflict = REPLACE)
    suspend fun addSearchQuery(searchQuery: SearchQuery)

    @Query("SELECT * FROM search_query WHERE timestamp < :timestamp")
    suspend fun getInvalidSearchQueries(timestamp: Long): List<SearchQuery>

    @Query("SELECT * FROM search_query")
    suspend fun getAllSearchQueries(): List<SearchQuery>

    @Transaction
    suspend fun deleteInvalidSearchQueries(timestamp: Long) {
        val queries = getAllSearchQueries()
        queries.forEach {
            if (it.timeStamp < timestamp) {
                deleteSearchQueryByName(it.queryName)
            }
        }
    }

    @Query("DELETE FROM search_query")
    suspend fun deleteAllSearchQueries()

    @Query("DELETE FROM search_query WHERE queryName = :queryName")
    suspend fun deleteSearchQueryByName(queryName: String)


}