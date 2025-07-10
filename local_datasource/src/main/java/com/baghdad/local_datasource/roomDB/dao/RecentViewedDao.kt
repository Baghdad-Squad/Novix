package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.RecentViewed
import com.baghdad.local_datasource.roomDB.entity.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {

    @Upsert()
    suspend fun upsertRecentViewed(recentViewed: RecentViewed)


    @Query("DELETE FROM RecentViewed WHERE mediaId = :mediaId")
    suspend fun deleteRecentViewed(mediaId: Long)


    @Query("DELETE FROM RecentViewed")
    suspend fun clearAllRecentViewed()


    @Query("SELECT * FROM RecentViewed ORDER BY time DESC")
    fun getAllRecentViewed(): Flow<List<RecentViewed>>


    @Query("SELECT * FROM RecentViewed WHERE mediaType = :mediaType ORDER BY time DESC")
    fun getRecentViewedByType(mediaType: String): Flow<List<RecentViewed>>


    @Query("SELECT EXISTS(SELECT 1 FROM RecentViewed WHERE mediaId = :mediaId)")
    suspend fun exists(mediaId: Long): Boolean


    @Query("SELECT COUNT(*) FROM RecentViewed")
    suspend fun getCount(): Int


    @Transaction
    @Query("""
        SELECT * FROM RecentViewed 
        LEFT JOIN Movie ON RecentViewed.mediaId = Movie.id AND RecentViewed.mediaType = 'movie'
        LEFT JOIN TvShow ON RecentViewed.mediaId = TvShow.id AND RecentViewed.mediaType = 'tv'
        ORDER BY RecentViewed.time DESC
    """)
    fun getRecentViewedWithDetails(): Flow<List<RecentViewedWithDetails>>


    @Transaction
    @Query("""
        SELECT * FROM RecentViewed
        ORDER BY time DESC 
        LIMIT 10
    """)
    suspend fun getLastTenRecentViewedItems(): List<RecentViewedWithDetails>


}

data class RecentViewedWithDetails(
    @Embedded
    val recentViewed: RecentViewed,

    @Embedded(prefix = "movie_")
    val movie: Movie? = null,

    @Embedded(prefix = "tv_")
    val tvShow: TvShow? = null
)