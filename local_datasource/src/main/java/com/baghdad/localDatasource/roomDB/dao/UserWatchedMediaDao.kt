package com.baghdad.localDatasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.localDatasource.roomDB.entity.UserWatchedMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWatchedMediaDao {

    @Upsert()
    suspend fun upsertUserWatchedMedia(userWatchedMedia: UserWatchedMedia)

    @Query("SELECT * FROM UserWatchedMedia WHERE userId = :userId and contentType = 'MOVIE' ORDER BY viewedAt DESC LIMIT :pageSize OFFSET :offset")
    suspend fun getPagedUserWatchedMediaMovies(
        userId: Long,
        pageSize: Int,
        offset: Int
    ): List<UserWatchedMedia>

    @Query("SELECT * FROM UserWatchedMedia WHERE userId = :userId and contentType = 'TV_SHOW' ORDER BY viewedAt DESC LIMIT :pageSize OFFSET :offset")
    suspend fun getPagedUserWatchedMediaTvShows(
        userId: Long,
        pageSize: Int,
        offset: Int
    ): List<UserWatchedMedia>

    @Query("SELECT * FROM UserWatchedMedia WHERE userId = :userId ORDER BY viewedAt DESC")
    fun observeUserWatchedMedia(userId: Long): Flow<List<UserWatchedMedia>>

    @Query("SELECT * FROM UserWatchedMedia WHERE userId = :userId and contentType = 'MOVIE' ORDER BY viewedAt DESC")
    fun getUserWatchedMediaMovies(userId: Long): Flow<List<UserWatchedMedia>>

    @Query("SELECT * FROM UserWatchedMedia WHERE userId = :userId and contentType = 'TV_SHOW' ORDER BY viewedAt DESC")
    fun getUserWatchedMediaTvShows(userId: Long): Flow<List<UserWatchedMedia>>
}
