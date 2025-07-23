package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TopRatedMovie

@Dao
interface TopRatedDao {
    @Upsert
    suspend fun upsertMovies(movies: List<TopRatedMovie>)

    @Query(
        """ SELECT * from TopRatedMovie LIMIT :pageSize OFFSET :offset"""
    )
    suspend fun getTopRatedMoves(
        pageSize: Int,
        offset: Int,
    ): List<TopRatedMovie>
}
// order by after offset