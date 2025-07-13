package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Movie
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Upsert()
    suspend fun upsertRecentlyViewed(recentlyViewed: RecentlyViewed)


    @Query("DELETE FROM RecentlyViewed WHERE contentId = :contentId")
    suspend fun deleteRecentlyViewed(contentId: Long)


    @Query("DELETE FROM RecentlyViewed")
    suspend fun clearAllRecentlyViewed()


    @Query("SELECT * FROM RecentlyViewed")
    fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>>


    @Query("SELECT * FROM RecentlyViewed WHERE contentType = :mediaType ORDER BY viewedAt DESC")
    fun getRecentlyViewedByType(mediaType: String): Flow<List<RecentlyViewed>>


    @Query("SELECT EXISTS(SELECT 1 FROM RecentlyViewed WHERE contentId = :mediaId)")
    suspend fun exists(mediaId: Long): Boolean


    @Query("SELECT COUNT(*) FROM RecentlyViewed")
    suspend fun getCount(): Int


    @Transaction
    @Query(
        """
        SELECT * FROM RecentlyViewed 
        LEFT JOIN Movie ON RecentlyViewed.contentId = Movie.id AND RecentlyViewed.contentType = 'movie'
        LEFT JOIN TvShow ON RecentlyViewed.contentId = TvShow.id AND RecentlyViewed.contentType = 'tv'
        ORDER BY RecentlyViewed.viewedAt DESC
    """
    )
    fun getRecentlyViewedWithDetails(): Flow<List<RecentlyViewedWithDetails>>


    @Transaction
    @Query(
        """
        SELECT * FROM RecentlyViewed
        ORDER BY viewedAt DESC 
    """
    )
    fun getLastTenRecentlyViewedItems(): Flow<List<RecentlyViewedWithDetails>>

}

data class RecentlyViewedWithDetails(
    @Embedded
    val recentlyViewed: RecentlyViewed,

    @Embedded(prefix = "movie_")
    val movie: Movie? = null,

    @Embedded(prefix = "tv_")
    val tvShow: TvShow? = null
)