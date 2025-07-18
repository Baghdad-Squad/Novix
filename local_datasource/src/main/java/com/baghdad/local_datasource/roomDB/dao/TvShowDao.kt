package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.repository.model.SearchQueryDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Upsert
    suspend fun upsertTvShow(tvShow: TvShow)

    @Upsert
    suspend fun upsertTvShows(tvShows: List<TvShow>)

    @Query("DELETE FROM TvShow WHERE id = :id")
    suspend fun deleteTvShowByID(id: Long)

    @Query("DELETE FROM TvShow")
    suspend fun deleteAll()

    @Query("SELECT * FROM TvShow WHERE id = :id")
    fun getTvShowById(id: Long): TvShow

    @Query("SELECT * FROM TvShow")
    fun getAllTvShow(): Flow<List<TvShow>>

    @Query(
        """
    SELECT t.* FROM TvShow t
    INNER JOIN search_query sq ON t.id = sq.mediaId
    WHERE sq.queryName = :queryName AND sq.mediaType = :mediaType
    LIMIT :pageSize OFFSET :offset
"""
    )
    suspend fun getTvShowsFromSearchQuery(
        queryName: String,
        pageSize: Int,
        offset: Int,
        mediaType: String = SearchQueryDto.MediaType.TV_SHOW.name
    ): List<TvShow>
}